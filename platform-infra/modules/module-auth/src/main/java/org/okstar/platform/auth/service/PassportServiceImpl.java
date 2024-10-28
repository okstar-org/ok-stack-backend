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

package org.okstar.platform.auth.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.auth.domain.AuthSession;
import org.okstar.platform.auth.keycloak.AuthzClientManager;
import org.okstar.platform.auth.keycloak.BackUserManager;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.exception.OkRuntimeException;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.core.rpc.RpcAssert;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.system.dto.BackUser;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.rpc.SysProfileRpc;
import org.okstar.platform.system.sign.*;

import java.util.Objects;
import java.util.Optional;

import static org.okstar.platform.core.account.AccountDefines.BindType.email;
import static org.okstar.platform.core.account.AccountDefines.BindType.phone;


@Slf4j
@ApplicationScoped
public class PassportServiceImpl implements PassportService {

    @Inject
    BackUserManager backUserManager;
    @Inject
    AuthzClientManager authzClientManager;
    @Inject
    AuthSessionService authSessionService;

    @Inject
    @RestClient
    SysProfileRpc sysProfileRpc;
    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @Override
    public synchronized SignUpResult signUp(SignUpForm form) {
        log.info("signUp:{}", form);
        // 验证参数
        validateParam(form);

        Optional<SysAccountDTO> account0 = sysAccountRpc.findByEmail(form.getAccount());
        if (account0.isPresent()) {
            throw new OkRuntimeException("帐号已存在！");
        }

        //初始化系统帐号
        SignUpResult signUpResult = RpcAssert.isTrue(sysAccountRpc.signUp(form));
        Log.infof("signUp=>%s", signUpResult);

        return signUpResult;
    }

    @Override
    public void signDown(Long accountId) {
        log.info("signDown:{}", accountId);

        Optional<SysAccountDTO> account0 = sysAccountRpc.findById(accountId);
        if (account0.isEmpty()) {
            return;
        }

        //删除认证信息
        boolean backUser = backUserManager.deleteUser(account0.get().getUsername());
        Log.infof("Sign down auth account:%s=>%s", accountId, backUser);
        OkAssert.isTrue(backUser, "Sign down auth account failed");

        //删除系统帐号
        sysAccountRpc.signDown(accountId);
        Log.infof("Sign down system account:%s", accountId);
    }

    @Override
    public AuthorizationResult signIn(SignInForm signInForm) {
        Log.debugf("signIn:%s", signInForm);
        String account = signInForm.getAccount();

        //获取帐号
        Log.infof("Get account: %s", account);
        SysAccountDTO accountDTO = getAccount(account);
        Log.debugf("getAccount=> %s", accountDTO);

        if (accountDTO == null) {
            throw new NotFoundException("帐号不存在！");
        }

        RpcResult<String> lastedPassword = sysAccountRpc.lastPassword(accountDTO.getId());
        String pwd = RpcAssert.isTrue(lastedPassword);
        OkAssert.isTrue(OkStringUtil.equals(pwd, signInForm.getPassword()), "密码不正确！");

        Optional<BackUser> backUser = backUserManager.getUser(accountDTO.getUsername());
        if (backUser.isEmpty()) {
            /**
             * KC不存在用户，则创建新的用户
             */
            SysProfileDTO profileDTO = sysProfileRpc.getByAccount(accountDTO.getId());
            OkAssert.notNull(profileDTO, "用户信息不完整！");

            BackUser addUser = new BackUser();
            addUser.setId(String.valueOf(accountDTO.getUid()));
            addUser.setUsername(accountDTO.getUsername());
            addUser.setFirstName(profileDTO.getFirstName());
            addUser.setLastName(profileDTO.getLastName());
            addUser.setEmail(profileDTO.getEmail());
            addUser.setPhone(profileDTO.getPhone());

            addUser.setEnabled(true);

            backUserManager.addUser(addUser);
            backUser = Optional.of(addUser);

            Log.infof("User is initialized to ldap successfully. {username:%s uid:%s} ", addUser.getUsername(), addUser.getId());
        }

        //密码不存在，则创建
        boolean hasPassword = backUserManager.hasPassword(accountDTO.getUsername());
        if (!hasPassword) {
            backUserManager.resetPassword(accountDTO.getUsername(), signInForm.getPassword());
        }

        BackUser backUser1 = backUser.get();
        accountDTO.setUid(backUser1.getId());
        sysAccountRpc.setUid(backUser1.getUsername(), accountDTO.getUid());
        sysAccountRpc.sync(backUser1.getUsername());

        AuthorizationResult result = authzClientManager.authorization(accountDTO.getUsername(), signInForm.getPassword());
        result.setUsername(accountDTO.getUsername());

        AuthSession session = buildAuthSession(signInForm, accountDTO, result);
        authSessionService.create(session, accountDTO.getId());

        return result;
    }

    private static AuthSession buildAuthSession(SignInForm signInForm, SysAccountDTO account0, AuthorizationResult result) {
        AuthSession sess = new AuthSession();
        sess.setUsername(account0.getUsername());
        sess.setDeviceType(signInForm.getDeviceType());
        sess.setLoginType(signInForm.getType());
        sess.setGrantType(signInForm.getGrantType());
        sess.setAccessToken(result.getAccessToken());
        sess.setExpiresIn(result.getExpiresIn());
        sess.setRefreshToken(result.getRefreshToken());
        sess.setRefreshExpiresIn(result.getRefreshExpiresIn());
        sess.setSessionState(result.getSession_state());
        return sess;
    }

    @Override
    public AuthorizationResult refresh(String refreshToken) {
        return authzClientManager.refresh(refreshToken);
    }

    @Override
    public void signOut(String accessToken) {
        authzClientManager.revoke(accessToken);
    }

    @Override
    public SysAccountDTO getAccount(String account) {
        return sysAccountRpc.getByAccount(account).orElse(null);
    }

    @Override
    public void updatePassword(PasswordUpdateForm updateForm) {
        OkAssert.hasText(updateForm.getUsername(), "用户名不能为空！");
        OkAssert.hasText(updateForm.getNewPassword(), "新密码不能为空！");
        OkAssert.isTrue(Objects.equals(updateForm.getNewPassword(), updateForm.getConfirmPassword()), "确认密码不正确！");
        backUserManager.resetPassword(updateForm.getUsername(), updateForm.getNewPassword());
    }

    @Override
    public void forgot(ForgotForm form) {
        OkAssert.notNull(form.getAccountType(), "帐号类型不能为空！");
        OkAssert.hasText(form.getAccount(), "帐号不能为空！");
        SysAccountDTO account = getAccount(form.getAccount());
        OkAssert.notNull(account, "帐号不存在！");
        backUserManager.forgot(account.getUsername());
    }

    /**
     * 验证参数
     *
     * @param signUpForm
     */
    public void validateParam(SignUpForm signUpForm) {
        if (signUpForm.getAccountType() == email && !signUpForm.getAccount().matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
            throw new OkRuntimeException("邮箱格式错误");
        }
        if (signUpForm.getAccountType() == phone && !signUpForm.getAccount().matches("^1[3456789]{1}[0-9]{9}$")) {
            throw new OkRuntimeException("手机号格式错误");
        }
    }

}
