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
import org.okstar.platform.auth.backend.AuthzClientManager;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.core.rpc.RpcAssert;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.org.dto.OrgStaffFragment;
import org.okstar.platform.org.rpc.OrgStaffRpc;
import org.okstar.platform.system.dto.BackUser;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.rpc.SysBackUserManagerRpc;
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
    @RestClient
    SysBackUserManagerRpc backUserManager;
    @Inject
    AuthzClientManager authzClientManager;


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
        Log.infof("保存到人员帐号=>%s", added);

        BackUser user = BackUser.builder()
                .username(signUpResult.getUsername())
                .firstName(form.getFirstName())
                .lastName(form.getLastName())
                .password(form.getPassword())
                .build();

        if (form.getAccountType() == email) {
            user.setEmail(form.getAccount());
        }

        BackUser backUser = backUserManager.add(user);
        log.info("Added user:{}", backUser.getUsername());
        return signUpResult;
    }

    @Override
    public void signDown(Long accountId) {
        log.info("signDown:{}", accountId);

        SysAccountDTO account0 = RpcAssert.isTrue(sysAccountRpc.findById(accountId));

        //删除认证信息
        boolean backUser = backUserManager.delete(account0.getUsername());
        Log.infof("Sign down auth account:%s=>%s", accountId, backUser);
        OkAssert.isTrue(backUser, "Sign down auth account failed");

        //删除系统帐号
        Boolean aTrue = RpcAssert.isTrue(sysAccountRpc.signDown(accountId));
        Log.infof("Sign down system account:%s=>%s", accountId, aTrue);
        OkAssert.isTrue(aTrue, "Delete system account failed");
    }

    @Override
    public AuthorizationResult signIn(SignInForm signInForm) {
        Log.infof("signIn:%s", signInForm);
        String account = signInForm.getAccount();

        //获取帐号
        SysAccountDTO account0 = getAccount(account);
        Log.debugf("getAccount=>%s", account0);
        if (account0 == null) {
            throw new NotFoundException("帐号不存在！");
        }

        //从后端系统获取用户
        Optional<BackUser> backUser = backUserManager.get(account0.getUsername());
        if (backUser.isEmpty()) {
            //不存在则创建
            RpcResult<String> lastedPassword = sysAccountRpc.lastPassword(account0.getId());
            String pwd = RpcAssert.isTrue(lastedPassword);
            OkAssert.isTrue(OkStringUtil.equals(pwd, signInForm.getPassword()), "密码不正确！");

            BackUser addUser = new BackUser();
            addUser.setId(String.valueOf(account0.getId()));
            addUser.setUsername(account0.getUsername());
            if (signInForm.getType() == email) {
                addUser.setEmail(signInForm.getAccount());
            } else if (signInForm.getType() == phone) {
                addUser.setAttributes(Map.of("phone", List.of(signInForm.getAccount())));
            }
            addUser.setPassword(pwd);

            BackUser added = backUserManager.add(addUser);
            Log.infof("User:%s is initialized to ldap successfully.", added.getUsername());
        }
        AuthorizationResult result = authzClientManager.authorization(account0.getUsername(), signInForm.getPassword());
        result.setUsername(account0.getUsername());
        return result;
    }

    @Override
    public AuthorizationResult refresh(String refreshForm) {
        return authzClientManager.refresh(refreshForm);
    }

    @Override
    public void signOut(String accessToken) {
        authzClientManager.revoke(accessToken);
    }

    @Override
    public SysAccountDTO getAccount(String account) {
        return RpcAssert.isTrue(sysAccountRpc.getByAccount(account));
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
