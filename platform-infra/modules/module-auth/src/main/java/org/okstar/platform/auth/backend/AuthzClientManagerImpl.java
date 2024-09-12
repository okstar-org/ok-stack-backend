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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.quarkus.logging.Log;
import io.quarkus.oidc.client.OidcClientConfig;
import io.quarkus.oidc.client.OidcClients;
import io.quarkus.oidc.client.Tokens;
import io.quarkus.oidc.client.runtime.OidcClientRecorder;
import io.quarkus.oidc.client.runtime.OidcClientsConfig;
import io.quarkus.oidc.common.runtime.OidcCommonConfig;
import io.quarkus.runtime.TlsConfig;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
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
import org.okstar.platform.system.rpc.SysKeycloakConfDTO;
import org.okstar.platform.system.rpc.SysKeycloakRpc;
import org.okstar.platform.system.sign.AuthorizationResult;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
class AuthzClientManagerImpl implements AuthzClientManager {

    @Inject
    Vertx vertx;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    @RestClient
    SysKeycloakRpc sysKeycloakRpc;

    /**
     * 1.初始化 Authz Client(登录认证)
     */
    public AuthzClient ensureAuthzClient() {
        Log.debugf("EnsureAuthzClient ...");
        SysKeycloakConfDTO conf = sysKeycloakRpc.getKeycloakConf();
        Log.infof("Get keycloak conf: %s", conf);

        Configuration configuration = new Configuration(
                conf.getAuthServerUrl(),
                conf.getRealm(),
                conf.getClientId(),
                Map.of("secret", conf.getClientSecret()),
                null
        );
        return AuthzClient.create(configuration);
    }

    /**
     * 2.初始化 OidcClients
     */
    public OidcClients ensureOidcClient() {
        Log.debugf("EnsureOidcClient ...");

        SysKeycloakConfDTO conf = sysKeycloakRpc.getKeycloakConf();
        Log.infof("Get keycloak conf: %s", conf);

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

        return recorder.setup(csc, tls, () -> vertx);
    }

    @Override
    public AuthorizationResult authorization(String username, String password) {
        OkAssert.hasText(username, "username is empty");
        OkAssert.hasText(password, "password is empty");

        try {
            AuthzClient authzClient = ensureAuthzClient();
            AuthorizationRequest request = new AuthorizationRequest();
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
                ObjectNode node = null;
                try {
                    node = objectMapper.readValue((cause.getBytes()), ObjectNode.class);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
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
                            throw new NotAuthorizedException(response);
                        case 403:
                            throw new ForbiddenException(response);
                    }
                }
            }
            throw new OkRuntimeException("服务异常，请稍后再试！");
        }
    }

    @Override
    public AuthorizationResult refresh(String refreshToken) {
        Log.infof("refresh:%s", refreshToken);

        try (var clients = ensureOidcClient(); var client = clients.getClient()) {
            Uni<Tokens> uni = client.refreshTokens(refreshToken);
            Tokens tokens = uni.subscribeAsCompletionStage().get();
            long expiresIn = (tokens.getAccessTokenExpiresAt() * 1000 - OkDateUtils.getTime()) / 1000;
            return AuthorizationResult.builder()
                    .accessToken(tokens.getAccessToken())
                    .expiresIn(expiresIn)
                    .refreshToken(tokens.getRefreshToken())
                    .refreshExpiresIn(tokens.getRefreshTokenTimeSkew())
                    .build();
        } catch (InterruptedException | ExecutionException | IOException e) {
            Log.errorf(e, "Refresh token error!");
            throw new OkRuntimeException("Refresh token error:" + e.getMessage());
        }
    }

    @Override
    public Boolean revoke(String accessToken) {
        Log.infof("Revoke access token:%s", accessToken);
        try (var clients = ensureOidcClient(); var client = clients.getClient()) {
            Uni<Boolean> uni = client.revokeAccessToken(accessToken);
            Boolean aBoolean = uni.subscribe().asCompletionStage().get();
            Log.infof("revoke:%s=>%s", accessToken, aBoolean);
            return aBoolean;
        } catch (InterruptedException | ExecutionException | IOException e) {
            throw new OkRuntimeException("Revoke token error:" + e.getMessage());
        }
    }
}
