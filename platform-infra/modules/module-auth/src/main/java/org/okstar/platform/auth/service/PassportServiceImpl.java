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

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.auth.backend.AuthzClientManager;
import org.okstar.platform.auth.backend.BackUser;
import org.okstar.platform.auth.backend.BackUserManager;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.utils.IdUtils;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.system.sign.*;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.vo.SysAccount0;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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

        SignUpResult resultDto = RpcAssert.isTrue(sysAccountRpc.signUp(signUpForm));

        BackUser user = BackUser.builder()
                .username(resultDto.getUsername())
                .id(("%s_%s").formatted(resultDto.getUserId(), IdUtils.makeUuid()))
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
    public SignInResult signIn(SignInForm signInForm) {
        String account = signInForm.getAccount();

        //判断帐号类型
        AccountDefines.BindType bindType = account.indexOf("@") > 0 ? AccountDefines.BindType.email : AccountDefines.BindType.phone;
        SysAccount0 userDto = RpcAssert.isTrue(sysAccountRpc.findByBind(
                signInForm.getIso(),
                bindType,
                signInForm.getAccount()));

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
