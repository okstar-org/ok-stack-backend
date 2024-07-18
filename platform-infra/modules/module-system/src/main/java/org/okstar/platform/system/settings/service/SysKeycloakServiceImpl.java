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

package org.okstar.platform.system.settings.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.authorization.*;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.utils.OkIdUtils;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.system.kv.rpc.SysKeycloakConfDTO;
import org.okstar.platform.system.settings.domain.SysSetKv;
import org.okstar.platform.system.settings.mapper.SysSetKVMapper;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Transactional
@ApplicationScoped
public class SysKeycloakServiceImpl implements SysKeycloakService {
    //Keycloak 用户名和密码
    private static final String DEFAULT_KC_USERNAME = "admin";
    private static final String DEFAULT_KC_PASSWORD = "okstar";

    @Inject
    SysSetKVMapper kvMapper;


    @ConfigProperty(name = "quarkus.oidc.auth-server-url",
            defaultValue = "http://localhost:18043/realms/okstar")
    private String authServerUrl;
    private String serverUrl;
    private String realm;


    @ConfigProperty(name = "quarkus.oidc.client-id",
            defaultValue = "okstack")
    private String clientId;

    @Inject
    ObjectMapper objectMapper;

    //配置组
    private String confGroup;


    void startup(@Observes StartupEvent event) {
        try {
            Log.infof("auth-server-url: %s", authServerUrl);
            URI url = new URI(authServerUrl);

            if (url.getPort() <= 0) {
                serverUrl = url.getScheme() + "://" + url.getHost();
            } else {
                serverUrl = url.getScheme() + "://" + url.getHost() + ":" + url.getPort();
            }
            Log.infof("server-url: %s", serverUrl);

            String path = url.getPath();
            if (OkStringUtil.isEmpty(path)) {
                throw new IllegalArgumentException("Is invliad auth server url:" + authServerUrl);
            }

            //realms/okstar
            String[] split = path.split("/");
            if (split.length != 3 || OkStringUtil.isEmpty(split[2].trim())) {
                throw new IllegalArgumentException("Is invliad auth server url:" + authServerUrl);
            }

            realm = split[2];
            Log.infof("Using realm: %s", realm);

        } catch (Exception e) {
            throw new RuntimeException("Unable to resolve auth server url", e);
        }

        confGroup = realm + "/" + clientId + "-quarkus.keycloak.admin-client";
        Log.debugf("Keycloak config prefix: %s", confGroup);

        initKeycloakConfig();
        String realm = initRealm();
        Log.infof("Initialized realm=>%s", realm);
    }


    @Override
    public void initClient(RealmRepresentation realm, String clientId) {
        Log.infof("Initialize client: %s for realm:%s", clientId, realm.getRealm());

        ClientRepresentation client = getClient(realm.getRealm(), clientId);
        if (client != null) {
            Log.infof("Found client: %s for realm:%s", clientId, realm);
            return;
        }

        Optional<String> clientSecret = getClientSecret();
        OkAssert.isTrue(clientSecret.isPresent(), "Unable to find client secret");

        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setEnabled(true);
        clientRepresentation.setClientId(clientId);
        clientRepresentation.setName(OkStringUtil.toCamelCase(clientId));
        clientRepresentation.setClientAuthenticatorType("client-secret");
        clientRepresentation.setSecret(clientSecret.get());

        clientRepresentation.setRootUrl("http://localhost:9100");
        clientRepresentation.setBaseUrl("http://localhost:9100");
        clientRepresentation.setAdminUrl("http://localhost:9100");
        clientRepresentation.setWebOrigins(List.of("http://localhost:9100"));
        clientRepresentation.setRedirectUris(List.of("*"));
        clientRepresentation.setAuthorizationServicesEnabled(true);
        clientRepresentation.setStandardFlowEnabled(true);
        clientRepresentation.setFrontchannelLogout(true);
        clientRepresentation.setDirectGrantsOnly(true);
        clientRepresentation.setDirectAccessGrantsEnabled(true);

        ResourceServerRepresentation authorization = new ResourceServerRepresentation();
        authorization.setName(clientId + "-authorization");
//        authorization.setClientId(clientId);
        authorization.setDecisionStrategy(DecisionStrategy.UNANIMOUS);
        authorization.setPolicyEnforcementMode(PolicyEnforcementMode.ENFORCING);
        authorization.setAllowRemoteResourceManagement(true);

        ResourceRepresentation res = new ResourceRepresentation();
        res.setName("Default Resource");
        res.setDisplayName("Default Resource");
        res.setUris(Set.of("/*"));
        res.setType("urn:%s:resources:default".formatted(clientId));
        authorization.setResources(List.of(res));

        PolicyRepresentation policy = new PolicyRepresentation();
        policy.setName("Default Policy");
        policy.setType("client");
        try {
            policy.setConfig(Map.of("clients", objectMapper.writeValueAsString(Set.of(clientId))));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        policy.setLogic(Logic.POSITIVE);
        authorization.setPolicies(List.of(policy));
        clientRepresentation.setAuthorizationSettings(authorization);

        realm.setClients(List.of(clientRepresentation));
        Log.infof("Initialized client: %s", clientId);
    }


    @Override
    public ClientRepresentation getClient(String realm, String clientId) {
        try (Keycloak kc = openKeycloak()) {
            RealmRepresentation realmRepresentation = getRealm(kc, realm);
            if (realmRepresentation == null) {
                return null;
            }
            List<ClientRepresentation> clients = realmRepresentation.getClients();
            if (clients == null) {
                return null;
            }
            for (ClientRepresentation client : clients) {
                if (client.getClientId().equals(this.clientId)) {
                    return client;
                }
            }
            return null;
        }
    }

    @Override
    public List<String> listRealms() {
        try (Keycloak kc = openKeycloak()) {
            RealmsResource realms = kc.realms();
            return realms.findAll().stream().map(e -> e.getRealm()).toList();
        }
    }

    @Override
    public void removeRealm() {
        try (Keycloak kc = openKeycloak()) {
            RealmsResource realms = kc.realms();
            realms.realm(realm).remove();
        }
    }

    @Override
    public String initRealm() {
        Log.infof("Initialize realm: %s", realm);
        try (Keycloak kc = openKeycloak()) {
            RealmsResource realms = kc.realms();
            RealmRepresentation realmRepresentation = getRealm(kc, realm);
            if (realmRepresentation != null) {
                Log.infof("Found existed realm: %s", realm);
                return realmRepresentation.getRealm();
            }

            realmRepresentation = new RealmRepresentation();
            realmRepresentation.setRealm(realm);
            realmRepresentation.setDisplayName(realm);
            realmRepresentation.setEnabled(true);

            initClient(realmRepresentation, clientId);

            // 创建realm
            realms.create(realmRepresentation);

            Log.infof("Created realm: %s", realmRepresentation.getRealm());
            return realmRepresentation.getRealm();
        }
    }


    private RealmRepresentation getRealm(Keycloak kc, String realm) {
        RealmsResource realms = kc.realms();
        for (RealmRepresentation realmRepresentation : realms.findAll()) {
            if (OkStringUtil.equalsIgnoreCase(realmRepresentation.getRealm(), realm)) {
                return realmRepresentation;
            }
        }
        return null;
    }


    @Override
    public void clearConfig() {
        kvMapper.delete("grouping", confGroup);
    }

    /**
     * # Configuration file
     *
     * @link https://quarkus.io/guides/security-oidc-configuration-properties-reference
     */
    @Override
    public void initKeycloakConfig() {
        String serverUrlKey = confGroup + ".server-url";
        long server = kvMapper.find("k", serverUrlKey).count();
        if (server <= 0) {
            SysSetKv kv = SysSetKv.builder()
                    .grouping(confGroup)
                    .name("Authorization Server Url")
                    .k(serverUrlKey)
                    .v(serverUrl)
                    .comment("Keycloak server URL")
                    .build();
            kvMapper.persist(kv);
        }

        String realmKey = confGroup + ".realm";
        long realm = kvMapper.find("k", realmKey).count();
        if (realm <= 0) {
            SysSetKv kv = SysSetKv.builder()
                    .grouping(confGroup)
                    .name("Realm")
                    .k(realmKey)
                    .v("master")
                    .comment("Realm.")
                    .build();
            kvMapper.persist(kv);
        }

        String clientIdKey = confGroup + ".client-id";
        long clientId = kvMapper.find("k", clientIdKey).count();
        if (clientId <= 0) {
            SysSetKv kv = SysSetKv.builder()
                    .grouping(confGroup)
                    .name("Client Id")
                    .k(clientIdKey)
                    .v("admin-cli")
                    .comment("The admin client id for the Keycloak.")
                    .build();
            kvMapper.persist(kv);
        }

        String usernameKey = confGroup + ".username";
        long username = kvMapper.find("k", usernameKey).count();
        if (username <= 0) {
            SysSetKv kv = SysSetKv.builder()
                    .grouping(confGroup)
                    .name("Username")
                    .k(usernameKey)
                    .v(DEFAULT_KC_USERNAME)
                    .comment("The username of the Keycloak.")
                    .build();
            kvMapper.persist(kv);
        }

        String passwordKey = confGroup + ".password";
        long pwd = kvMapper.find("k", passwordKey).count();
        if (pwd <= 0) {
            SysSetKv kv = SysSetKv.builder()
                    .grouping(confGroup)
                    .name("Password")
                    .k(passwordKey)
                    .v(DEFAULT_KC_PASSWORD)
                    .comment("The password of the Keycloak.")
                    .build();
            kvMapper.persist(kv);
        }

        //为客户端生成的密码
        String clientSecretKey = confGroup + ".client-secret";
        long clientSecret = kvMapper.find("k", clientSecretKey).count();
        if (clientSecret <= 0) {
            SysSetKv kv = SysSetKv.builder()
                    .grouping(confGroup)
                    .name("Client Secret")
                    .k(clientSecretKey)
                    .v(OkIdUtils.makeUuid())
                    .comment("The default secret of the Keycloak.")
                    .build();
            kvMapper.persist(kv);
        }
    }


    @Override
    public String testConfig() {
        Log.info("Test OIDC config");
        try (var kc = openKeycloak()) {
            if (kc == null) return null;
            return kc.serverInfo().getInfo().getSystemInfo().getVersion();
        }
    }


    public Optional<String> getClientSecret() {
        String clientSecretKey = confGroup + ".client-secret";
        return kvMapper.find("k", clientSecretKey).stream()
                .map(e -> e.getV())
                .findFirst();
    }

    public SysKeycloakConfDTO getOidcConfig() {
        var list = kvMapper.find("grouping", confGroup).list();
        if (list.isEmpty()) {
            return null;
        }

        SysKeycloakConfDTO dto = new SysKeycloakConfDTO();
        dto.setRealm(realm);
        dto.setClientId(clientId);

        for (SysSetKv item : list) {
            if (item.getV() == null)
                continue;
            if (OkStringUtil.equals(item.getK(), confGroup + ".server-url")) {
                dto.setAuthServerUrl(item.getV());
            } else if (OkStringUtil.equals(item.getK(), confGroup + ".client-secret")) {
                dto.setClientSecret(item.getV());
            }
        }
        return dto;
    }

    @Override
    public UsersResource getUsersResource(Keycloak keycloak) {
        RealmResource realm = keycloak.realms().realm(this.realm);
        return realm.users();

    }

    @Override
    public Keycloak openKeycloak() {
        var list = kvMapper.find("grouping", confGroup).list();
        if (list.isEmpty()) {
            return null;
        }
        var builder = KeycloakBuilder.builder();
        for (SysSetKv item : list) {
            if (item.getV() == null)
                continue;
            if (OkStringUtil.equals(item.getK(), confGroup + ".server-url")) {
                builder.serverUrl(item.getV());
            } else if (OkStringUtil.equals(item.getK(), confGroup + ".username")) {
                builder.username(item.getV());
            } else if (OkStringUtil.equals(item.getK(), confGroup + ".password")) {
                builder.password(item.getV());
            } else if (OkStringUtil.equals(item.getK(), confGroup + ".realm")) {
                builder.realm(item.getV());
            } else if (OkStringUtil.equals(item.getK(), confGroup + ".client-id")) {
                builder.clientId(item.getV());
            }
        }
        return builder
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
                .build();
    }
}
