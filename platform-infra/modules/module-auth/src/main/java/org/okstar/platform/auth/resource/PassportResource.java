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

package org.okstar.platform.auth.resource;

import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.auth.service.PassportService;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.controller.OkBaseController;
import org.okstar.platform.system.vo.SignUpForm;
import org.okstar.platform.system.vo.SignUpResultDto;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 通行
 */
@Slf4j
@Path("passport")
public class PassportResource extends OkBaseController {

    @Inject
    PassportService passportService;

    @POST
    @Path("signUp")
    public Res<SignUpResultDto> signUp(SignUpForm signUpForm) {
        log.info("signUp:{}", signUpForm);
        var resultDto = passportService.signUp(signUpForm);
        log.info("resultDto=>{}", resultDto);
        return Res.ok(signUpForm, resultDto);
    }
}
