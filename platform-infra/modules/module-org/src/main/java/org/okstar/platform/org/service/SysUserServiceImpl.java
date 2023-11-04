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

package org.okstar.platform.org.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.okstar.platform.common.core.constant.UserConstants;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.datasource.OkAbsService;
import org.okstar.platform.org.account.SysAccount;
import org.okstar.platform.org.account.SysAccountMapper;
import org.okstar.platform.org.domain.SysPost;
import org.okstar.platform.org.account.SysAccountBind;
import org.okstar.platform.org.dto.SignUpForm;
import org.okstar.platform.org.dto.SignUpResultDto;
import org.okstar.platform.org.mapper.*;
import org.okstar.platform.org.utils.RepositoryUtil;
import org.springframework.util.Assert;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;
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
public class SysUserServiceImpl extends OkAbsService implements SysUserService {

    @Inject
    SysAccountMapper sysAccountMapper;
    @Inject
    SysUserBindMapper sysUserBindMapper;

    //    @Inject
    private SysRoleMapper roleMapper;

    //    @Inject
    private SysPostMapper postMapper;


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
    public List<SysAccount> selectUserList(SysAccount user) {
        PanacheQuery<SysAccount> p = RepositoryUtil.queryOfExample(sysAccountMapper, user);
        return p.list();
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override

    public List<SysAccount> selectAllocatedList(SysAccount user) {
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

    public List<SysAccount> selectUnallocatedList(SysAccount user) {
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
    public SysAccount selectUserByUserName(String userName) {
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
    public SysAccount selectUserById(Long userId) {
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
        return "";
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
    public String checkPhoneUnique(SysAccount user) {
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
    public String checkEmailUnique(SysAccount user) {
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
    public void checkUserAllowed(SysAccount user) {
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
    public int updateUser(SysAccount user) {
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
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    public int updateUserStatus(SysAccount user) {
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
    public int updateUserProfile(SysAccount user) {
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
    public String importUser(List<SysAccount> userList, Boolean isUpdateSupport, String operName) {
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
    public List<SysAccount> selectUserListByRoleKey(String roleKey) {
//        return userMapper.selectUserListByRoleKey(roleKey);
        return Collections.emptyList();
    }

    @Override
    public SignUpResultDto signUp(SignUpForm signUpForm) {
        Log.infof("signUp:%s", signUpForm);

        Assert.hasText(signUpForm.getIso());
        Assert.hasText(signUpForm.getPassword());
        Assert.notNull(signUpForm.getAccountType());
        Assert.notNull(signUpForm.getAccount());

        SysAccount sysUser = new SysAccount();
        sysUser.setIso(signUpForm.getIso());
        sysUser.setFirstName(signUpForm.getFirstName());
        sysUser.setLastName(signUpForm.getLastName());
        sysUser.setUsername(RandomStringUtils.randomAlphanumeric(12));
        sysAccountMapper.persist(sysUser);
        Log.infof("User have been saved successfully.=>%s",
                sysUser.getUsername(), sysUser);

        SysAccountBind bind = new SysAccountBind();
        switch (signUpForm.getAccountType()) {
            case email -> {
                bind.setBindType(AccountDefines.BindType.email);
                bind.setBindValue(signUpForm.getAccount());
            }
            case phone -> {
                bind.setBindType(AccountDefines.BindType.phone);
                //添加手机号绑定
                try {
                    PhoneNumberUtil util = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber number;
                    number = util.parse(signUpForm.getAccount(), signUpForm.getIso());
                    bind.setBindValue(util.format(number, INTERNATIONAL));
                } catch (NumberParseException e) {
                    throw new OkRuntimeException("号码无法被解析", e);
                }
            }
        }


        bind.setAccount(sysUser);
        sysUserBindMapper.persist(bind);

        return SignUpResultDto.builder()
                .username(sysUser.getUsername())
                .userId(sysUser.id)
                .build();
    }

    @Override
    public List<SysAccount> findAll() {
        return sysAccountMapper.findAll().list();
    }

    @Override
    public SysAccount get(Long id) {
        return sysAccountMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        sysAccountMapper.deleteById(id);
    }

    @Override
    public void delete(SysAccount sysUser) {
        sysAccountMapper.delete(sysUser);
    }


    @Override
    public OkPageResult<SysAccount> findPage(OkPageable pageable) {
        var all = sysAccountMapper.findAll();
        var query = all.page(Page.of(pageable.getPageNumber(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public void save(SysAccount sysUser) {
        sysAccountMapper.persist(sysUser);
    }

    @Override
    public boolean isAdmin(Long userId) {
        return Objects.equals(1L, userId);
    }
}
