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
import org.okstar.platform.auth.keycloak.AuthzClientManager;
import org.okstar.platform.auth.domain.AuthSession;
import org.okstar.platform.auth.keycloak.BackUserManager;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.exception.OkRuntimeException;
import org.okstar.platform.core.rpc.RpcAssert;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.org.dto.OrgStaffFragment;
import org.okstar.platform.org.rpc.OrgStaffRpc;
import org.okstar.platform.system.dto.BackUser;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;

import org.okstar.platform.system.sign.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.okstar.platform.core.account.AccountDefines.BindType.email;
import static org.okstar.platform.core.account.AccountDefines.BindType.phone;


@Slf4j
@ApplicationScoped
public class PassportServiceImpl implements PassportService {

    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;
    @Inject
    @RestClient
    OrgStaffRpc orgStaffRpc;
    @Inject
    BackUserManager backUserManager;
    @Inject
    AuthzClientManager authzClientManager;
    @Inject
    AuthSessionService authSessionService;

    @Override
    public synchronized SignUpResult signUp(SignUpForm form) {
        log.info("signUp:{}", form);
        // 验证参数
        validateParam(form);
        //初始化系统帐号
        SignUpResult signUpResult = RpcAssert.isTrue(sysAccountRpc.signUp(form));
        Log.infof("signUp=>%s", signUpResult);

        OrgStaffFragment staff = new OrgStaffFragment();
        staff.setName(form.getName());
        staff.setFirstName(form.getFirstName());
        staff.setLastName(form.getLastName());
        staff.setIso(form.getIso());

        switch (form.getAccountType()) {
            case email -> staff.setEmail(form.getAccount());
            case phone -> staff.setPhone(form.getAccount());
        }

        var added = RpcAssert.isTrue(orgStaffRpc.add(signUpResult.getAccountId(), staff));
        Log.infof("Add account[%s] to staff=>%s", signUpResult.getUsername(), added);
        if (added == null) {
            throw new OkRuntimeException("Unable to save account!");
        }

        BackUser user = BackUser.builder()
                .username(signUpResult.getUsername())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .password(form.getPassword())
                .build();

        if (form.getAccountType() == email) {
            user.setEmail(form.getAccount());
        }

        BackUser backUser = backUserManager.addUser(user);
        log.info("Added user:{}", backUser);
        if (backUser == null) {
            throw new OkRuntimeException("Unable to save account to authentication backend server!");
        }

        return signUpResult;
    }

    @Override
    public void signDown(Long accountId) {
        log.info("signDown:{}", accountId);

        SysAccountDTO account0 = sysAccountRpc.findById(accountId);

        //删除认证信息
        boolean backUser = backUserManager.deleteUser(account0.getUsername());
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

        //从后端系统获取用户
        Optional<BackUser> backUser = backUserManager.getUser(accountDTO.getUsername());
        if (backUser.isEmpty()) {
            //不存在则创建
            RpcResult<String> lastedPassword = sysAccountRpc.lastPassword(accountDTO.getId());
            String pwd = RpcAssert.isTrue(lastedPassword);
            OkAssert.isTrue(OkStringUtil.equals(pwd, signInForm.getPassword()), "密码不正确！");

            BackUser addUser = new BackUser();
            addUser.setId(String.valueOf(accountDTO.getUid()));
            addUser.setUsername(accountDTO.getUsername());
            if (signInForm.getType() == email) {
                addUser.setEmail(signInForm.getAccount());
            } else if (signInForm.getType() == phone) {
                addUser.setAttributes(Map.of("phone", List.of(signInForm.getAccount())));
            }
            addUser.setPassword(pwd);

            var added = backUserManager.addUser(addUser);
            Log.infof("User is initialized to ldap successfully. {username:%s uid:%s} ", added.getUsername(), added.getId());
            backUser = Optional.of(added);
        }

        BackUser backUser1 = backUser.get();
        accountDTO.setUid(backUser1.getId());
        sysAccountRpc.setUid(backUser1.getUsername(), accountDTO.getUid());
        sysAccountRpc.syncDb2Ldap(backUser1.getUsername());

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
