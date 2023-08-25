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

package org.okstar.platform.system.service.impl;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import org.junit.jupiter.api.Test;
import org.okstar.platform.system.dto.SignUpForm;
import org.okstar.platform.system.dto.SignUpResultDto;
import org.okstar.platform.system.service.ISysUserService;

import javax.inject.Inject;

@QuarkusTest
class SysUserServiceImplTest {


    @Inject
    ISysUserService sysUserService;

    @Test
    void signUp() {
        SignUpForm form = new SignUpForm();
        form.setTs(1L);
        form.setPhone("18910221510");
        form.setIso("CN");
        form.setPassword("18910221510");
        SignUpResultDto resultDto = sysUserService.signUp(form);
        Log.infof("result=>%s", resultDto);
        Assert.assertNotNull(resultDto);
        Assert.assertNotNull(resultDto.getUsername());
    }
}
