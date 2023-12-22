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

import io.quarkus.logging.Log;
import io.quarkus.oidc.client.Tokens;
import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import org.okstar.platform.auth.service.PassportService;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.system.sign.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * 通行
 */
@Path("passport")
public class PassportResource extends OkCommonResource {

    @Inject
    Tokens tokens;

    @Inject
    PassportService passportService;

    @POST
    @Path("signUp")
    public Res<SignUpResult> signUp(SignUpForm signUpForm) {
        Log.infof("signUp:%s", signUpForm);
        var resultDto = passportService.signUp(signUpForm);
        Log.infof("resultDto:%s", resultDto);
        return Res.ok(signUpForm, resultDto);
    }

    @POST
    @Path("signIn")
    @Blocking
    public Res<SignInResult> signIn(SignInForm signInForm) {
        Log.infof("signIn:%s", signInForm);
        var resultDto = passportService.signIn(signInForm);
        Log.infof("resultDto=>%s", resultDto);
        return Res.ok(signInForm, resultDto);
    }

    @POST
    @Path("signOut")
    public Res<Boolean> signOut() {
        String accessToken = tokens.getAccessToken();
        passportService.signOut(accessToken);
        return Res.ok(Req.empty(), true);
    }

    @POST
    @Path("refresh")
    public Res<RefreshResult> refresh(RefreshForm refreshForm) {

        RefreshResult result = RefreshResult.builder()
                .accessToken(tokens.getAccessToken())
                .exp(tokens.getAccessTokenExpiresAt())
                .refreshToken(tokens.getRefreshToken())
                .refresh(refreshForm.getRefresh())
                .build();

        Log.infof("result:%s", result);
        return Res.ok(refreshForm, result);
    }
}
