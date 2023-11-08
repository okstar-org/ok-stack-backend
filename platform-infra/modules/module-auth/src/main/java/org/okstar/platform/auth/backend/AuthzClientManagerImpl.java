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

package org.okstar.platform.auth.backend;

import io.quarkus.logging.Log;
import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.Tokens;
import io.smallrye.mutiny.Uni;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.okstar.platform.system.sign.RefreshForm;
import org.okstar.platform.system.sign.SignInResult;
import org.springframework.util.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
class AuthzClientManagerImpl implements  AuthzClientManager {
    @Inject
    AuthzClient authzClient;

    @Inject
    OidcClient oidcClient;

    @Override
    public SignInResult authorization(String username, String password){
        Assert.hasText(username,"username is empty");
        Assert.hasText(password, "password is empty");
        AuthorizationRequest request = new AuthorizationRequest();
        var response = authzClient.authorization(username.toLowerCase(), password).authorize(request);
        return SignInResult.builder()
                .session_state(response.getSessionState())
                .token(response.getToken())
                .expires_in(response.getExpiresIn())
                .refresh_token(response.getRefreshToken())
                .refresh_expires_in(response.getRefreshExpiresIn())
                .token_type(response.getTokenType())
                .build();
    }

    @Override
    public SignInResult refresh(RefreshForm refreshForm) {
        Uni<Tokens> uni = oidcClient.refreshTokens(refreshForm.getRefreshToken());
        try {
            Tokens tokens = uni.subscribeAsCompletionStage().get();
            return SignInResult.builder()
                    .token(tokens.getAccessToken())
                    .expires_in(tokens.getAccessTokenExpiresAt())
                    .refresh_token(tokens.getRefreshToken())
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void revoke(String accessToken) {
        Log.infof("Revoke access token:%s", accessToken);
        oidcClient.revokeAccessToken(accessToken);
    }
}
