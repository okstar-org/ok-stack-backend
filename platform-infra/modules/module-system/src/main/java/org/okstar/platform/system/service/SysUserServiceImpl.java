/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
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

package org.okstar.platform.system.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.okstar.platform.common.core.constant.UserConstants;
import org.okstar.platform.common.core.enums.UserBindDefined;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.core.web.page.Pageable;
import org.okstar.platform.common.datasource.OkAbsService;
import org.okstar.platform.system.domain.*;
import org.okstar.platform.system.dto.SignUpForm;
import org.okstar.platform.system.dto.SignUpResultDto;
import org.okstar.platform.system.mapper.*;
import org.okstar.platform.system.utils.RepositoryUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;


/**
 * 用户 业务层处理
 */
@Slf4j
@Singleton
@Transactional
public class SysUserServiceImpl extends OkAbsService  implements SysUserService {

    @Inject
    SysUserRepository sysUserRepository;
    @Inject
    SysUserBindRepository sysUserBindRepository;

    //    @Inject
    private SysRoleMapper roleMapper;

    //    @Inject
    private SysPostMapper postMapper;

    //    @Inject
    private SysUserRoleMapper userRoleMapper;

    //    @Inject
    private SysUserPostMapper userPostMapper;

    //    @Inject
    private ISysConfigService configService;

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public List<SysUser> selectUserList(SysUser user) {
        PanacheQuery<SysUser> p = RepositoryUtil.queryOfExample(sysUserRepository, user);
        return p.list();

    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override

    public List<SysUser> selectAllocatedList(SysUser user) {
//        return userMapper.selectAllocatedList(user);
        return Collections.emptyList();


    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override

    public List<SysUser> selectUnallocatedList(SysUser user) {
//        return userMapper.selectUnallocatedList(user);
        return Collections.emptyList();

    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
//        return userMapper.selectUserByUserName(userName);
        return null;

    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(Long userId) {
//        return userMapper.selectUserById(userId);
        return null;
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = roleMapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (OkStringUtil.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = postMapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (OkStringUtil.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
//        int count = userMapper.checkUserNameUnique(userName);
//        if (count > 0) {
//            return UserConstants.NOT_UNIQUE;
//        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
//        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
//        SysUser info = userMapper.checkPhoneUnique(user.getPhonenumber());
//        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
//            return UserConstants.NOT_UNIQUE;
//        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkEmailUnique(SysUser user) {
//        Long userId = StringUtils.isNull(user.getId()) ? -1L : user.getId();
//        SysUser info = userMapper.checkEmailUnique(user.getEmail());
//        if (StringUtils.isNotNull(info) && info.getUserId().longValue() != userId.longValue()) {
//            return UserConstants.NOT_UNIQUE;
//        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
//        if (StringUtils.isNotNull(user.getId()) && user.isAdmin()) {
//            throw new CustomException("不允许操作超级管理员用户");
//        }
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateUser(SysUser user) {
//        Long userId = user.getId();
//        // 删除用户与角色关联
//        userRoleMapper.deleteUserRoleByUserId(userId);
//        // 新增用户与角色管理
//        insertUserRole(user);
//        // 删除用户与岗位关联
//        userPostMapper.deleteUserPostByUserId(userId);
//        // 新增用户与岗位管理
//        insertUserPost(user);
//        return userMapper.updateUser(user);

        return 0;
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Override
    @Transactional
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.deleteUserRoleByUserId(userId);
        insertUserRole(userId, roleIds);
    }

    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysUser user) {
//        return userMapper.updateUser(user);

        return 0;
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserProfile(SysUser user) {
//        return userMapper.updateUser(user);

        return 0;
    }

    /**
     * 修改用户头像
     *
     * @param userName 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    public boolean updateUserAvatar(String userName, String avatar) {
//        return userMapper.updateUserAvatar(userName, avatar) > 0;
        return false;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int resetPwd(SysUser user) {
//        return userMapper.updateUser(user);
        return 0;
    }

    /**
     * 重置用户密码
     *
     * @param userName 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    public int resetUserPwd(String userName, String password) {
//        return userMapper.resetUserPwd(userName, password);
        return 0;
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        Long[] roles = user.getRoleIds();
        if (OkStringUtil.isNotNull(roles)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roles) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(user.id);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 新增用户岗位信息
     *
     * @param user 用户对象
     */
    public void insertUserPost(SysUser user) {
        Long[] posts = user.getPostIds();
        if (OkStringUtil.isNotNull(posts)) {
            // 新增用户与岗位管理
            List<SysUserPost> list = new ArrayList<SysUserPost>();
            for (Long postId : posts) {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.id);
                up.setPostId(postId);
                list.add(up);
            }
            if (list.size() > 0) {
                userPostMapper.batchUserPost(list);
            }
        }
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (OkStringUtil.isNotNull(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = new ArrayList<SysUserRole>();
            for (Long roleId : roleIds) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                list.add(ur);
            }
            if (list.size() > 0) {
                userRoleMapper.batchUserRole(list);
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserById(Long userId) {
//        // 删除用户与角色关联
//        userRoleMapper.deleteUserRoleByUserId(userId);
//        // 删除用户与岗位表
//        userPostMapper.deleteUserPostByUserId(userId);
//        return userMapper.deleteUserById(userId);

        return 0;
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
//        for (Long userId : userIds) {
//            checkUserAllowed(new SysUser(userId));
//        }
//        // 删除用户与角色关联
//        userRoleMapper.deleteUserRole(userIds);
//        // 删除用户与岗位关联
//        userPostMapper.deleteUserPost(userIds);
//        return userMapper.deleteUserByIds(userIds);

        return 0;
    }

    /**
     * 导入用户数据
     *
     * @param userList        用户数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName        操作用户
     * @return 结果
     */
    @Override
    public String importUser(List<SysUser> userList, Boolean isUpdateSupport, String operName) {
//        if (StringUtils.isNull(userList) || userList.size() == 0) {
//            throw new CustomException("导入用户数据不能为空！");
//        }
//        int successNum = 0;
//        int failureNum = 0;
//        StringBuilder successMsg = new StringBuilder();
//        StringBuilder failureMsg = new StringBuilder();
//        String password = configService.selectConfigByKey("sys.user.initPassword");
//        for (SysUser user : userList) {
//            try {
//                // 验证是否存在这个用户
//                SysUser u = userMapper.selectUserByUserName(user.getUserName());
//                if (StringUtils.isNull(u)) {
//                    user.setPassword(SecurityUtils.encryptPassword(password));
//                    user.setCreateBy(operName);
//                    this.insertUser(user);
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 导入成功");
//                } else if (isUpdateSupport) {
//                    user.setUpdateBy(operName);
//                    this.updateUser(user);
//                    successNum++;
//                    successMsg.append("<br/>" + successNum + "、账号 " + user.getUserName() + " 更新成功");
//                } else {
//                    failureNum++;
//                    failureMsg.append("<br/>" + failureNum + "、账号 " + user.getUserName() + " 已存在");
//                }
//            } catch (Exception e) {
//                failureNum++;
//                String msg = "<br/>" + failureNum + "、账号 " + user.getUserName() + " 导入失败：";
//                failureMsg.append(msg + e.getMessage());
//                log.error(msg, e);
//            }
//        }
//        if (failureNum > 0) {
//            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
//            throw new CustomException(failureMsg.toString());
//        } else {
//            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
//        }
//        return successMsg.toString();
        return "";
    }

    /**
     * 某角色下的用户列表
     *
     * @param roleKey
     * @return
     */
    @Override
    public List<SysUser> selectUserListByRoleKey(String roleKey) {
//        return userMapper.selectUserListByRoleKey(roleKey);
        return Collections.emptyList();
    }

    @Override
    public SignUpResultDto signUp(SignUpForm signUpForm) {
        Log.infof("signUp:%s", signUpForm);

        String random = RandomStringUtils.randomAlphanumeric(12);
        SysUser sysUser = new SysUser();
        sysUser.setUsername(random);
//        sysUser.setPassword(new BCryptPasswordEncoder().encode(signUpForm.getPassword()));
        sysUserRepository.persist(sysUser);

        //添加手机号绑定
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber number;
        try {
            number = util.parse(signUpForm.getPhone(),signUpForm.getIso());
        } catch (NumberParseException e) {
            throw new OkRuntimeException("号码无法被解析", e);
        }

        SysUserBind bind = new SysUserBind();
        bind.setBindType(UserBindDefined.BindType.phone);
        bind.setBindValue(util.format(number, INTERNATIONAL));
        bind.setIsValid(true);
        bind.setUser(sysUser);
        sysUserBindRepository.persist(bind);

        return SignUpResultDto.builder()
                .username(sysUser.getUsername())
                .build();
    }

    @Override
    public List<SysUser> findAll() {
        return sysUserRepository.findAll().list();
    }

    @Override
    public SysUser get(Long id) {
        return sysUserRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        sysUserRepository.deleteById(id);
    }

    @Override
    public void delete(SysUser sysUser) {
        sysUserRepository.delete(sysUser);
    }

    @Override
    public List<SysUser> findPage(Pageable page) {
        PanacheQuery<SysUser> all = sysUserRepository.findAll();
        all.page(Page.of(page.getPage(), page.getSize()));
        return all.list();
    }

    @Override
    public SysUser save(SysUser sysUser) {
        sysUserRepository.persist(sysUser);
        return sysUser;
    }

    @Override
    public boolean isAdmin(Long userId) {
        return Objects.equals(1L, userId);
    }
}
