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

package org.okstar.platform.auth.service;

import io.quarkus.logging.Log;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.auth.form.LoginBody;
import org.okstar.platform.common.core.constant.UserConstants;
import org.okstar.platform.common.core.enums.UserStatus;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.common.security.domain.LoginUser;
import org.okstar.platform.common.security.service.TokenService;
import org.okstar.platform.system.dto.SysUserDto;
import org.okstar.platform.system.rpc.SysUserRpc;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


/**
 * 登录校验方法
 */
@Slf4j
@ApplicationScoped
public class SysLoginService {
    @Inject
    private TokenService tokenService;

    @Inject
    @RestClient
    private SysUserRpc sysUserRpc;


    /**
     * 登录
     */
    public LoginUser login(LoginBody loginBody) {
        Log.infof("login:%s", loginBody);

        //TODO(gaojie): 暂时接收用户名
        String username = loginBody.getLoginKey();
        String password = loginBody.getPassword();

        // 用户名或密码为空 错误
        if (OkStringUtil.isAnyBlank(username, password)) {
//            recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new OkRuntimeException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
//            recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new OkRuntimeException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
//            recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new OkRuntimeException("用户名不在指定范围");
        }
        // 查询用户信息
        SysUserDto user = sysUserRpc.findByUsername(username);
        org.springframework.util.Assert.isTrue(user != null, "登录用户：" + username + " 不存在");

        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
//            recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new OkRuntimeException("对不起，您的账号：" + username + " 已被删除");
        }

        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
//            recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new OkRuntimeException("对不起，您的账号：" + username + " 已停用");
        }

//        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
//            recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码错误");
//            throw new BaseException("用户不存在/密码错误");
//        }
//        recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");


        // 角色集合
//        Set<String> roles = permissionService.getRolePermission(user.getId());
        // 权限集合
//        Set<String> permissions = permissionService.getMenuPermission(user.getId());

        LoginUser loginUser = new LoginUser();
        OkBeanUtils.copyPropertiesTo(user, loginUser);
//        loginUser.setRoles(roles);
//        loginUser.setPermissions(permissions);
        return loginUser;
    }

    public void logout(String loginName) {
//        recordLogininfor(loginName, Constants.LOGOUT, "退出成功");
    }


    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
//    public void recordLogininfor(String username, String status, String message) {
//        SysLogininfor logininfor = new SysLogininfor();
//        logininfor.setUserName(username);
////        logininfor.setIpaddr(OkWebUtil.getIpAddr(request));
//        logininfor.setMsg(message);
//        // 日志状态
//        if (OkStringUtil.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
//            logininfor.setStatus("0");
//        } else if (Constants.LOGIN_FAIL.equals(status)) {
//            logininfor.setStatus("1");
//        }
//        //TODO: 存储日志
//        log.warn("暂无实现日志存储。。。");
////        remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
//    }
}
