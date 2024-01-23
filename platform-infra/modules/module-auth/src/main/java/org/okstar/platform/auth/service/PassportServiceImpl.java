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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.auth.backend.AuthzClientManager;
import org.okstar.platform.auth.backend.BackUser;
import org.okstar.platform.auth.backend.BackUserManager;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.org.dto.OrgStaffFragment;
import org.okstar.platform.org.rpc.OrgStaffRpc;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.sign.*;
import org.okstar.platform.system.vo.SysAccount0;

import java.util.Objects;
import java.util.Optional;

import static org.okstar.platform.common.core.defined.AccountDefines.BindType.email;
import static org.okstar.platform.common.core.defined.AccountDefines.BindType.phone;

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


    @Override
    public synchronized SignUpResult signUp(SignUpForm form) {
        log.info("signUp:{}", form);

        //初始化系统帐号
        SignUpResult signUpResult = RpcAssert.isTrue(sysAccountRpc.signUp(form));
        Log.infof("signUp=>%s", signUpResult.getUsername());

        OrgStaffFragment staff = new OrgStaffFragment();
        staff.setName(form.getName());
        staff.setFirstName(form.getFirstName());
        staff.setLastName(form.getLastName());
        staff.setIso(form.getIso());
        switch (form.getAccountType()) {
            case email -> staff.setEmail(form.getAccount());
            case phone -> staff.setPhone(form.getAccount());
        }
        var added = RpcAssert.isTrue(orgStaffRpc.add(staff));
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


        BackUser backUser = backUserManager.addUser(user);
        log.info("Added user:{}", backUser.getUsername());
        return signUpResult;
    }

    @Override
    public void signDown(Long accountId) {
        log.info("signDown:{}", accountId);

        SysAccount0 account0 = RpcAssert.isTrue(sysAccountRpc.findById(accountId));

        //删除认证信息
        boolean backUser = backUserManager.deleteUser(account0.getUsername());
        Log.infof("Sign down auth account:%s=>%s", accountId, backUser);
        OkAssert.isTrue(backUser, "Sign down auth account failed");

        //删除系统帐号
        Boolean aTrue = RpcAssert.isTrue(sysAccountRpc.signDown(accountId));
        Log.infof("Sign down system account:%s=>%s", accountId, aTrue);
        OkAssert.isTrue(aTrue, "Delete system account failed");
    }

    @Override
    public SignInResult signIn(SignInForm signInForm) {
        String account = signInForm.getAccount();
        Log.infof("signIn:%s", account);

        //判断帐号类型
        SysAccount0 account0 = getAccount(account);
        Log.debugf("Get Account info is:%s", account0);

        /**
         * 初始化LDAP用户
         */
        Optional<BackUser> backUser = backUserManager.getUser(account0.getUsername());
        if (backUser.isEmpty()) {
            RpcResult<String> lastedPassword = sysAccountRpc.lastPassword(account0.getId());
            String pwd = RpcAssert.isTrue(lastedPassword);

            BackUser addUser = new BackUser();
            addUser.setId(String.valueOf(account0.getId()));
            addUser.setUsername(account0.getUsername());
            addUser.setPassword(pwd);

            BackUser added = backUserManager.addUser(addUser);
            Log.infof("User:%s is initialized to ldap successfully.", added.getUsername());
        }
        return authzClientManager.authorization(account0.getUsername(), signInForm.getPassword());
    }

    @Override
    public SignInResult refresh(String refreshForm) {
        return authzClientManager.refresh(refreshForm);
    }

    @Override
    public void signOut(String accessToken) {
        authzClientManager.revoke(accessToken);
    }

    @Override
    public SysAccount0 getAccount(String account) {
        //判断帐号类型
        AccountDefines.BindType bindType = account.indexOf("@") > 0 ? email : phone;  //
        SysAccount0 account0 = RpcAssert.isTrue(sysAccountRpc.findByBind(AccountDefines.DefaultISO, bindType, account));
        if (account0 == null) {
            throw new OkRuntimeException("Account is not exist");
        }

        return account0;
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
        SysAccount0 account = getAccount(form.getAccount());
        OkAssert.notNull(account, "帐号不存在！");
        backUserManager.forgot(account.getUsername());
    }


}
