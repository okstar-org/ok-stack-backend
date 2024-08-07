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

package org.okstar.platform.auth.resource;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.okstar.platform.auth.service.PassportService;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.system.sign.*;

/**
 * 通行
 */
@Path("passport")
public class PassportResource extends OkCommonResource {


    @Inject
    PassportService passportService;

    @POST
    @Path("signUp")
    public Res<SignUpResult> signUp(SignUpForm signUpForm) {
        Log.infof("signUp:%s", signUpForm);
        var resultDto = passportService.signUp(signUpForm);
        Log.infof("resultDto=>%s", resultDto);
        return Res.ok(signUpForm, resultDto);
    }

    @POST
    @Path("signIn")
    public Res<AuthorizationResult> signIn(SignInForm signInForm) {
        Log.infof("signIn:%s", signInForm);
        var resultDto = passportService.signIn(signInForm);
        Log.infof("signIn=>%s", resultDto);
        return Res.ok(signInForm, resultDto);
    }

    @POST
    @Path("signOut")
    public Res<Boolean> signOut(@HeaderParam("Authorization")String accessToken) {
        Log.infof("signOut...");

        String username = getUsername();
        Log.infof("username:%s", username);

        Log.infof("accessToken=>%s", accessToken);
        passportService.signOut(accessToken);
        return Res.ok(Req.empty(), true);
    }

    @POST
    @Path("refresh")
    public Res<AuthorizationResult> refresh(RefreshForm refreshForm) {
        Log.infof("refreshToken:%s", refreshForm.getRefreshToken());
        AuthorizationResult result = passportService.refresh(refreshForm.getRefreshToken());
        Log.infof("refresh accessToken=>%s", result);
        return Res.ok(refreshForm, result);
    }




    /**
     * 忘记密码
     *
     * @param form
     * @return
     */
    @POST
    @Path("forgot")
    public Res<Boolean> forgot(ForgotForm form) {
        Log.infof("forgot:%s", form);
        passportService.forgot(form);
        return Res.ok(true);
    }

}
