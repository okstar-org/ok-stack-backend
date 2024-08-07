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

package org.okstar.platform.auth.backend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.logging.Log;
import io.quarkus.oidc.client.OidcClient;
import io.quarkus.oidc.client.OidcClientConfig;
import io.quarkus.oidc.client.OidcClients;
import io.quarkus.oidc.client.Tokens;
import io.quarkus.oidc.client.runtime.OidcClientRecorder;
import io.quarkus.oidc.client.runtime.OidcClientsConfig;
import io.quarkus.oidc.common.runtime.OidcCommonConfig;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.TlsConfig;
import io.quarkus.security.UnauthorizedException;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.authorization.client.util.HttpResponseException;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.json.OkJsonUtils;
import org.okstar.platform.system.kv.rpc.SysKeycloakConfDTO;
import org.okstar.platform.system.kv.rpc.SysKeycloakRpc;
import org.okstar.platform.system.sign.AuthorizationResult;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
class AuthzClientManagerImpl implements AuthzClientManager {

    @Inject
    Vertx vertx;
    @Inject
    OkJsonUtils jsonUtils;
    @Inject
    @RestClient
    SysKeycloakRpc sysKeycloakRpc;

    OidcClient oidcClient;

    AuthzClient authzClient;

    /**
     * 2.初始化oidcClient
     */
    public void startup(@Observes StartupEvent event) {

        SysKeycloakConfDTO conf = sysKeycloakRpc.getKeycloakConf();
        Log.infof("Get keycloak conf: %s", conf);

        Configuration configuration = new Configuration(
                conf.getAuthServerUrl(),
                conf.getRealm(),
                conf.getClientId(),
                Map.of("secret", conf.getClientSecret()),
                null
        );
        authzClient = AuthzClient.create(configuration);

        OidcClientConfig cc = new OidcClientConfig();
        cc.setClientId(conf.getClientId());
        cc.setAuthServerUrl(conf.getAuthServerUrl() + "/realms/" + conf.getRealm());

        OidcCommonConfig.Credentials credentials = new OidcCommonConfig.Credentials();
        credentials.clientSecret.setValue(conf.getClientSecret());
        cc.setCredentials(credentials);

        OidcClientRecorder recorder = new OidcClientRecorder();
        TlsConfig tls = new TlsConfig();
        tls.trustAll = true;
        OidcClientsConfig csc = new OidcClientsConfig();
        csc.defaultClient = cc;
        csc.namedClients = Map.of();

        OidcClients oidcClients = recorder.setup(csc, tls, () -> vertx);
        oidcClient = oidcClients.getClient();
    }


    @Override
    public AuthorizationResult authorization(String username, String password) {
        OkAssert.hasText(username, "username is empty");
        OkAssert.hasText(password, "password is empty");

        AuthorizationRequest request = new AuthorizationRequest();
        try {
            AuthorizationResponse response = authzClient.authorization(username.toLowerCase(), password).authorize(request);
            return AuthorizationResult.builder()
                    .session_state(response.getSessionState())
                    .accessToken(response.getToken())
                    .tokenType(response.getTokenType())
                    .expiresIn(response.getExpiresIn())
                    .refreshToken(response.getRefreshToken())
                    .refreshExpiresIn(response.getRefreshExpiresIn())
                    .build();
        } catch (Exception e) {
            Log.warnf(e, "认证异常！");

            if (e.getCause() instanceof HttpResponseException cause) {
                int statusCode = cause.getStatusCode();
                // 错误信息：{"error":"invalid_grant","error_description":"Account is not fully set up"}
                ObjectNode node = jsonUtils.asObject(new String(cause.getBytes()), ObjectNode.class);
                JsonNode description;
                if (node != null && (description = node.get("error_description")) != null) {
                    String error = node.get("error").asText();
                    Log.warnf("error:%s description: %s", error, description);
                    //400 / Bad Request / Body : {"error":"invalid_grant","error_description":"Account is not fully set up"}
                    Res<Object> error0 = Res.error(Req.empty(), description.asText());
                    Response response = Response
                            .status(Response.Status.fromStatusCode(statusCode))
                            .entity(error0)
                            .build();
                    switch (statusCode) {
                        case 400:
                            throw new BadRequestException(response);
                        case 401:
                            throw new UnauthorizedException(description.asText());
                        case 403:
                            throw new NotAuthorizedException(response);
                    }
                }
            }
            throw new OkRuntimeException("服务异常，请稍后再试！");
        }
    }

    @Override
    public AuthorizationResult refresh(String refreshToken) {
        Log.infof("refresh:%s", refreshToken);
        Uni<Tokens> uni = oidcClient.refreshTokens(refreshToken);
        try {
            Tokens tokens = uni.subscribeAsCompletionStage().get();
            long expiresIn = (tokens.getAccessTokenExpiresAt() * 1000 - OkDateUtils.getTime()) / 1000;
            return AuthorizationResult.builder()
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
    public Boolean revoke(String accessToken) {
        Log.infof("Revoke access token:%s", accessToken);
        Uni<Boolean> uni = oidcClient.revokeAccessToken(accessToken);
        try {
            Boolean aBoolean = uni.subscribe().asCompletionStage().get();
            Log.infof("revoke:%s=>%s", accessToken, aBoolean);
            return aBoolean;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
