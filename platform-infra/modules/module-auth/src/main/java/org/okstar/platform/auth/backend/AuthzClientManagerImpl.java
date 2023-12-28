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
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.utils.OkDateUtils;
import org.okstar.platform.system.sign.SignInResult;

import java.util.concurrent.ExecutionException;

@ApplicationScoped
class AuthzClientManagerImpl implements AuthzClientManager {
    @Inject
    AuthzClient authzClient;

    @Inject
    OidcClient oidcClient;

    @Override
    public SignInResult authorization(String username, String password) {
        OkAssert.hasText(username, "username is empty");
        OkAssert.hasText(password, "password is empty");
        AuthorizationRequest request = new AuthorizationRequest();
        var response = authzClient.authorization(username.toLowerCase(), password).authorize(request);
        return SignInResult.builder()
                .session_state(response.getSessionState())
                .accessToken(response.getToken())
                .tokenType(response.getTokenType())
                .expiresIn(response.getExpiresIn())
                .refreshToken(response.getRefreshToken())
                .refreshExpiresIn(response.getRefreshExpiresIn())
                .build();
    }

    @Override
    public SignInResult refresh(String refreshToken) {
        Uni<Tokens> uni = oidcClient.refreshTokens(refreshToken);
        try {
            Tokens tokens = uni.subscribeAsCompletionStage().get();
            long expiresIn = (tokens.getAccessTokenExpiresAt() * 1000 - OkDateUtils.getTime()) / 1000;
            return SignInResult.builder()
                    .accessToken(tokens.getAccessToken())
                    .expiresIn(expiresIn)
                    .refreshToken(tokens.getRefreshToken())
                    .refreshExpiresIn(tokens.getRefreshTokenTimeSkew())
                    .build();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void revoke(String accessToken) {
        Log.infof("Revoke access token:%s", accessToken);
        Uni<Boolean> uni = oidcClient.revokeAccessToken(accessToken);
        try {
            Boolean aBoolean = uni.subscribe().asCompletionStage().get();
            Log.infof("revoke:%s=>%s", accessToken, aBoolean);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
