/*
 * * Copyright (c) 2022 船山信息 chuanshaninfo.com
 * OkStack is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.org.connect.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.phone.OkPhoneUtils;
import org.okstar.platform.common.string.OkNameUtil;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.common.string.UserName;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.core.org.JobDefines;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.org.connect.api.Department;
import org.okstar.platform.org.connect.api.UserId;
import org.okstar.platform.org.connect.api.UserInfo;
import org.okstar.platform.org.connect.connector.OrgConnector;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.connect.exception.ConnectorException;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.staff.domain.OrgStaffPost;
import org.okstar.platform.org.staff.service.OrgStaffPostService;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.rpc.SysProfileRpc;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Transactional
@ApplicationScoped
public class ConnectorSyncServiceImpl implements ConnectorSyncService {
    @Inject
    OrgDeptService deptService;
    @Inject
    OrgStaffService staffService;
    @Inject
    OrgPostService postService;
    @Inject
    OrgStaffPostService staffPostService;
    @Inject
    OrgService orgService;

    @Inject
    @RestClient
    SysProfileRpc sysProfileRpc;
    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @Override
    public void sync(OrgConnector connect) throws ConnectorException {
        Log.infof("Synchronizing connector: %s...", connect.getType());

        AtomicInteger counter = new AtomicInteger(0);
        OrgIntegrateConf conf = connect.getConf();
        List<Department> departments = connect.getDepartmentList(conf.getRootDeptId());
        for (Department department : departments) {
            int level = counter.incrementAndGet();
            Log.infof("Sync department level: %s", level);
            syncDepartment(connect, department, level);
        }

        Log.infof("Synchronized connector: %s.", connect.getType());
    }

    void syncDepartment(OrgConnector connect, Department department, int level) throws ConnectorException {
        String name = department.getName();

        Long orgId = orgService.loadCurrent().id;

        Log.infof("Synchronizing department: %s", name);
        OrgDept dept = deptService.findByNameLevel(name, level);
        if (dept == null) {
            //保存部门到本地
            Log.warnf("Save department: %s", name);

            OrgDept localParent;
            if (department.getParentId().equals(connect.getConf().getRootDeptId())) {
                //获取自己的根部门
                localParent = deptService.loadRootByOrgId(orgId);
            } else {
                //获取远程的根部门
                Department parent = connect.getDepartment(department.getParentId());
                if (parent == null) {
                    Log.warnf("Parent department is null!");
                    return;
                }
                //获取本地上级目录
                localParent = deptService.findByNameLevel(parent.getName(), level - 1);
            }

            dept = new OrgDept();
            dept.setName(name);
            dept.setLevel(level);
            dept.setOrgId(orgId);
            dept.setParentId(localParent.id);
            dept.setDisabled(false);

            deptService.create(dept, 1L);
            Log.infof("Save department: %s level: %d", name, level);
        }

        //同步用户
        List<UserId> userIds = connect.getUserIdList(department);
        if (userIds == null) {
            Log.warnf("userIds is null!");
            return;
        }
        for (UserId userId : userIds) {
            UserInfo info = connect.getUserInfo(userId.getUserId());
            syncUser(dept, info);
        }
    }

    void syncUser(OrgDept department, UserInfo info) {
        Log.infof("Synchronizing user: %s", info.getName());
        if (OkStringUtil.isEmpty(info.getEmail()) && OkStringUtil.isEmpty(info.getMobilePhone())) {
            Log.infof("User: [%s] Both the phone number and email address are empty!");
            return;
        }

        SysAccountDTO accountDTO = null;
        AccountDefines.BindType bindType = null;
        String iso = AccountDefines.DefaultISO;
        if (OkStringUtil.isNotEmpty(info.getEmail())) {
            bindType = AccountDefines.BindType.email;
            Optional<SysAccountDTO> account = sysAccountRpc.findByEmail(info.getEmail());
            if (account.isPresent()) {
                Log.infof("User: [%s,%s] is existing!", info.getName(), info.getEmail());
                accountDTO = account.get();
            }
        }

        if (OkStringUtil.isNotEmpty(info.getMobilePhone())) {
            bindType = AccountDefines.BindType.phone;
            Optional<SysAccountDTO> account = sysAccountRpc.findByPhone(info.getMobilePhone(), iso);
            if (account.isPresent()) {
                Log.infof("User: [%s,%s] is existing!", info.getName(), info.getMobilePhone());
                accountDTO = account.get();
            }
        }

        if (bindType == null) {
            Log.warnf("Can not to determine bind type!");
            return;
        }

        //统一按照中文处理
        UserName userName = OkNameUtil.splitName(AccountDefines.DefaultLanguage, info.getName());

        OrgStaff staff;
        if (accountDTO == null) {
            //注册新用户
            Log.infof("Register user: %s by: %s", info.getEmail(), bindType);
            SignUpForm signUpForm = new SignUpForm();
            signUpForm.setAccountType(bindType);
            signUpForm.setAccount(bindType == AccountDefines.BindType.email ? info.getEmail() : info.getMobilePhone());
            signUpForm.setAvatar(info.getAvatar());
            signUpForm.setPassword(AccountDefines.DefaultPWD);
            signUpForm.setLanguage(AccountDefines.DefaultLanguage);
            signUpForm.setIso(AccountDefines.DefaultISO);
            signUpForm.setNickname(info.getNickname());

            RpcResult<SignUpResult> signUp = sysAccountRpc.signUp(signUpForm);
            if (!signUp.isSuccess()) {
                Log.errorf("Register user: %s is failed:%s!", info.getName(), signUp.getMsg());
                return;
            }

            accountDTO = sysAccountRpc.findByUsername(signUp.getData().getUsername()).get();
        }
        Optional<OrgStaff> orgStaff = staffService.getByAccountId(accountDTO.getId());
        if (orgStaff.isEmpty()) {
            //保存员工信息
            Log.infof("Saving staff: %s", info.getName());



            SysProfileDTO profile = new SysProfileDTO();
            profile.setGender(info.getGender());
            profile.setFirstName(userName.getFirstName());
            profile.setLastName(userName.getLastName());
            profile.setEmail(info.getEmail());
            profile.setPhone(OkPhoneUtils.canonical(info.getMobilePhone(), AccountDefines.DefaultISO));
            profile.setAccountId(accountDTO.getId());
            sysProfileRpc.save(accountDTO.getId(), profile);

            staff = new OrgStaff();
            staff.setPostStatus(JobDefines.PostStatus.employed);
            staff.setJoinedDate(OkDateUtils.now());
            staff.setAccountId(accountDTO.getId());
            staffService.create(staff, 1L);

            Log.infof("Saved staff: %s", info.getName());
            orgStaff = Optional.of(staff);
        }

        //绑定到岗位
        String position = info.getPosition();
        if (OkStringUtil.isNotEmpty(position)) {
            Log.infof("Save staff position info!");

            //获取本地岗位
            Optional<OrgPost> orgPost = postService.findByDeptAndName(department.id, position);
            if (orgPost.isEmpty()) {
                //保存新增岗位
                Log.infof("Save new position:%s", position);
                OrgPost post = new OrgPost();
                post.setName(position);
                post.setDeptId(department.id);
                postService.create(post, 1L);
                Log.infof("Saved post: %s", position);
                orgPost = Optional.of(post);
            }

            // 绑定到岗位
            OrgStaffPost staffPost = new OrgStaffPost();
            staffPost.setPostId(orgPost.get().id);
            staffPost.setStaffId(orgStaff.get().id);
            staffPostService.create(staffPost, 1L);
            //保存人员到岗位信息
            Log.infof("Bind staff: %s to position:%s", staffPost.getStaffId(), staffPost.getPostId());
        }

    }
}
