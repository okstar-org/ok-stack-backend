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
import org.keycloak.admin.client.Keycloak;
import org.okstar.platform.system.dto.SignUpForm;
import org.okstar.platform.system.dto.SignUpResultDto;
import org.okstar.platform.system.rpc.SysUserRpc;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class PassportServiceImpl implements PassportService {


    @Inject
    @RestClient
    SysUserRpc sysUserRpc;

    @Inject
    Keycloak keycloak;

    @PreDestroy
    public void closeKeycloak() {
        keycloak.close();
    }

    @Override
    public SignUpResultDto signUp(SignUpForm signUpForm) {
        log.info("signUp:{}", signUpForm);
        SignUpResultDto resultDto = sysUserRpc.signUp(signUpForm);
        log.info("resultDto=>{}", resultDto);
        return resultDto;
    }



}
