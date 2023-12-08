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
import org.okstar.platform.auth.backend.AuthzClientManager;
import org.okstar.platform.auth.backend.BackUser;
import org.okstar.platform.auth.backend.BackUserManager;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.sign.SignInForm;
import org.okstar.platform.system.sign.SignInResult;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;
import org.okstar.platform.system.vo.SysAccount0;
import org.springframework.util.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@Slf4j
@ApplicationScoped
public class PassportServiceImpl implements PassportService {


    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @Inject
    BackUserManager backUserManager;
    @Inject
    AuthzClientManager authzClientManager;


    @Override
    public SignUpResult signUp(SignUpForm signUpForm) {
        log.info("signUp:{}", signUpForm);

        //初始化系统帐号
        SignUpResult resultDto = RpcAssert.isTrue(sysAccountRpc.signUp(signUpForm));
        BackUser user = BackUser.builder()
                .username(resultDto.getUsername())
                .firstName(signUpForm.getFirstName())
                .lastName(signUpForm.getLastName())
                .password(signUpForm.getPassword())
                .build();
        if (signUpForm.getAccountType() == AccountDefines.BindType.email) {
            user.setEmail(signUpForm.getAccount());
        }

        BackUser backUser = backUserManager.addUser(user);
        log.info("Added user:{}", backUser.getUsername());

        return resultDto;
    }

    @Override
    public void signDown(Long accountId) {
        log.info("signDown:{}", accountId);

        SysAccount0 account0 = RpcAssert.isTrue(sysAccountRpc.findById(accountId));

        //删除认证信息
        boolean backUser = backUserManager.deleteUser(account0.getUsername());
        Log.infof("Sign down auth account:%s=>%s", accountId, backUser);
        Assert.isTrue(backUser, "Sign down auth account failed");

        //删除系统帐号
        Boolean aTrue = RpcAssert.isTrue(sysAccountRpc.signDown(accountId));
        Log.infof("Sign down system account:%s=>%s", accountId, aTrue);
        Assert.isTrue(aTrue, "Delete system account failed");
    }

    @Override
    public synchronized SignInResult signIn(SignInForm signInForm) {
        String account = signInForm.getAccount();
        Log.infof("signIn:%s", account);

        //判断帐号类型
        AccountDefines.BindType bindType = account.indexOf("@") > 0 ?
                AccountDefines.BindType.email : //
                AccountDefines.BindType.phone;  //

        SysAccount0 userDto = RpcAssert.isTrue(sysAccountRpc.findByBind(signInForm.getIso(), bindType, signInForm.getAccount()));
        if (userDto == null) {
            throw new OkRuntimeException("user is not exist");
        }

        /**
         * 初始化LDAP用户
         */
        Optional<BackUser> backUser = backUserManager.getUser(userDto.getUsername());
        if (backUser.isEmpty()) {
            RpcResult<String> lastedPassword = sysAccountRpc.lastPassword(userDto.getId());
            String pwd = RpcAssert.isTrue(lastedPassword);

            BackUser addUser = new BackUser();
            addUser.setId(String.valueOf(userDto.getId()));
            addUser.setUsername(userDto.getUsername());
            addUser.setPassword(pwd);

            BackUser added = backUserManager.addUser(addUser);
            Log.infof("User:%s is initialized to ldap successfully.", added.getUsername());
        }
        return authzClientManager.authorization(userDto.getUsername(), signInForm.getPassword());
    }

    @Override
    public SignInResult refresh(String refreshForm) {
        return authzClientManager.refresh(refreshForm);
    }

    @Override
    public void signOut(String accessToken) {
        authzClientManager.revoke(accessToken);
    }


}
