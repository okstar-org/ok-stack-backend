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
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.utils.OkIdUtils;
import org.okstar.platform.system.sign.SignInForm;
import org.okstar.platform.system.sign.AuthorizationResult;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;


@QuarkusTest
class PassportServiceImplTest {

    public static final String STRING = "okstar";

    @Inject
    PassportService passportService;

    /**
     * 注册用户
     */
    @Test
    @Order(1)
    void signUp() {
        String uuid = OkIdUtils.makeUuid();
        SignUpForm form = new SignUpForm();
        form.setTs(1L);

        //采用邮箱注册
        form.setAccountType(AccountDefines.BindType.email);
        form.setAccount("%s@okstar.org".formatted(uuid));

        form.setPassword(STRING);
        form.setFirstName("Ok");
        form.setLastName("Star");
        form.setIso(AccountDefines.DefaultISO);
        SignUpResult resultDto = passportService.signUp(form);
        Log.infof("result=>%s", resultDto);
        Assert.assertNotNull(resultDto);
        Assert.assertNotNull(resultDto.getUsername());
    }

    @Test
    void signIn() {
        SignInForm form = new SignInForm();
        form.setIso(AccountDefines.DefaultISO);
        form.setGrantType("password");
        form.setAccount("18910221510");
        form.setPassword("123456");
        AuthorizationResult result = passportService.signIn(form);
        Log.infof("result=>%s", result);
        Assert.assertNotNull(result);
    }
}
