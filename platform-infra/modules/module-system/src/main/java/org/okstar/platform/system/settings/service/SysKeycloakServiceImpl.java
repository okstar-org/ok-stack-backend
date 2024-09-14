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
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.rpc.SysKeycloakConfDTO;
import org.okstar.platform.system.settings.domain.SysConfIntegrationKeycloak;
import org.okstar.platform.system.settings.domain.SysProperty;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Transactional
@ApplicationScoped
public class SysKeycloakServiceImpl implements SysKeycloakService {
    //Keycloak 用户名和密码
    private static final String DEFAULT_KC_USERNAME = "admin";
    private static final String DEFAULT_KC_PASSWORD = "okstar";
    public static final String MASTER_REALM = "master";
    public static final String ADMIN_CLIENT_CLI = "admin-cli";

    @Inject
    SysPropertyService propertyService;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ExecutorService executorService;

    @ConfigProperty(name = "quarkus.oidc.auth-server-url",
            defaultValue = "http://localhost:18080/realms/okstar")
    private String authServerUrl;
    private String realm;
    private String serverUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id", defaultValue = "okstack")
    private String clientId;


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

            //["", "realms", "okstar"]
            String[] split = path.split("/");
            if (split.length != 3 || OkStringUtil.isEmpty(split[2].trim())) {
                throw new IllegalArgumentException("Is invliad auth server url:" + authServerUrl);
            }

            realm = split[2];
            Log.infof("Using realm: %s", realm);

        } catch (Exception e) {
            throw new RuntimeException("Unable to resolve auth server url", e);
        }

        SysConfIntegrationKeycloak conf = initConfig();
        executorService.execute(() -> {
            String value = null;
            do {
                try {
                    Log.infof("Initializing realm: %s", realm);
                    value = initRealm(conf, realm);
                    Log.infof("Initialized realm=>%s", realm);
                } catch (Exception e) {
                    Log.warnf("Unable to initialize realm: %s, Try again in 1 minute.", realm);
                    try {
                        TimeUnit.MINUTES.sleep(1);
                    } catch (InterruptedException ignored) {

                    }
                }
            } while (value == null);
        });
    }

    public void initClient(SysConfIntegrationKeycloak conf, RealmRepresentation realm, String clientId) {
        Log.infof("Initialize client: %s for realm:%s", clientId, realm.getRealm());

        ClientRepresentation client = getClient(realm.getRealm(), clientId);
        if (client != null) {
            Log.infof("Found client: %s for realm:%s", clientId, realm);
            return;
        }

        String clientSecret = conf.getClientSecret();
        OkAssert.isTrue(OkStringUtil.isNoneBlank(clientSecret), "Unable to find client secret");

        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setEnabled(true);
        clientRepresentation.setClientId(clientId);
        clientRepresentation.setName(OkStringUtil.toCamelCase(clientId));
        clientRepresentation.setClientAuthenticatorType("client-secret");
        clientRepresentation.setSecret(clientSecret);

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
            realms.findAll().forEach(r -> {
                if (!OkStringUtil.equals(r.getRealm(), MASTER_REALM)) {
                    Log.infof("Removing realm=%s", r.getRealm());
                    realms.realm(r.getRealm()).remove();
                }
            });
        }
    }

    public String initRealm(SysConfIntegrationKeycloak conf, String realm) {
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

            initClient(conf, realmRepresentation, clientId);

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
        SysConfIntegrationKeycloak keycloak = new SysConfIntegrationKeycloak();
        propertyService.deleteByGroup(keycloak.getGroup());
    }


    @Override
    public SysConfIntegrationKeycloak getConfig() {
        SysConfIntegrationKeycloak conf = new SysConfIntegrationKeycloak();
        List<SysProperty> kvs = propertyService.findByGroup(conf.getGroup());
        conf.addProperties(kvs);
        conf.setServerUrl(serverUrl);
        return conf;
    }

    /**
     * Configuration file
     */
    @Override
    public SysConfIntegrationKeycloak initConfig() {
        SysConfIntegrationKeycloak conf = new SysConfIntegrationKeycloak();
        conf.setServerUrl(serverUrl);
        conf.setUsername(DEFAULT_KC_USERNAME);
        conf.setPassword(DEFAULT_KC_PASSWORD);
        conf.setRealm(MASTER_REALM);
        conf.setClientId(ADMIN_CLIENT_CLI);

        var serverUrl = propertyService.findByKey(conf.getGroup(), conf.getRealm(), "server-url");
        if (serverUrl.isEmpty()) {
            SysProperty kv = SysProperty.builder()
                    .grouping(conf.getGroup())
                    .k("server-url")
                    .v(conf.getServerUrl())
                    .domain(conf.getRealm())
                    .build();
            propertyService.create(kv, 1L);
        }
        String realmKey = "realm";
        var realm = propertyService.findByKey(conf.getGroup(), conf.getRealm(), realmKey);
        if (realm.isEmpty()) {
            SysProperty kv = SysProperty.builder()
                    .grouping(conf.getGroup())
                    .k(realmKey)
                    .v(conf.getRealm())
                    .domain(conf.getRealm())
                    .build();
            propertyService.create(kv, 1L);
        }
        String clientIdKey = "client-id";
        var clientId = propertyService.findByKey(conf.getGroup(), conf.getRealm(), clientIdKey);
        if (clientId.isEmpty()) {
            SysProperty kv = SysProperty.builder()
                    .grouping(conf.getGroup())
                    .k(clientIdKey)
                    .v(conf.getClientId())
                    .domain(conf.getRealm())
                    .build();
            propertyService.create(kv, 1L);
        }

        String usernameKey = "username";
        var username = propertyService.findByKey(conf.getGroup(), conf.getRealm(), usernameKey);
        if (username.isEmpty()) {
            SysProperty kv = SysProperty.builder()
                    .grouping(conf.getGroup())
                    .k(usernameKey)
                    .v(conf.getUsername())
                    .domain(conf.getRealm())
                    .build();
            propertyService.create(kv, 1L);
        }

        String passwordKey = "password";
        var pwd = propertyService.findByKey(conf.getGroup(), conf.getRealm(), passwordKey);
        if (pwd.isEmpty()) {
            SysProperty kv = SysProperty.builder()
                    .grouping(conf.getGroup())
                    .k(passwordKey)
                    .v(conf.getPassword())
                    .domain(conf.getRealm())
                    .build();
            propertyService.create(kv, 1L);
        }

        String clientSecretKey = "client-secret";
        var clientSecret = propertyService.findByKey(conf.getGroup(), conf.getRealm(), clientSecretKey);
        if (clientSecret.isEmpty()) {
            //为客户端生成的密码
            String secret = OkIdUtils.makeUuid();
            conf.setClientSecret(secret);
            SysProperty kv = SysProperty.builder()
                    .grouping(conf.getGroup())
                    .k(clientSecretKey)
                    .v(secret)
                    .domain(conf.getRealm())
                    .build();
            propertyService.create(kv, 1L);
        } else {
            conf.setClientSecret(clientSecret.stream().findFirst().get().getV());
        }

        return conf;
    }


    @Override
    public String testConfig() {
        Log.info("Test OIDC config");
        try (var kc = openKeycloak()) {
            if (kc == null) return null;
            return kc.serverInfo().getInfo().getSystemInfo().getVersion();
        }
    }

    @Override
    public SysKeycloakConfDTO getOidcConfig() {
        SysConfIntegrationKeycloak config = getConfig();
        if (config == null) return null;
        SysKeycloakConfDTO dto = new SysKeycloakConfDTO();
        dto.setRealm(realm);
        dto.setClientId(clientId);
        dto.setAuthServerUrl(config.getServerUrl());
        dto.setClientSecret(config.getClientSecret());
        return dto;
    }

    @Override
    public UsersResource getUsersResource(Keycloak keycloak) {
        RealmResource realm = keycloak.realms().realm(this.realm);
        return realm.users();
    }

    @Override
    public Keycloak openKeycloak() {
        SysConfIntegrationKeycloak config = getConfig();
        if (config == null) {
            Log.warn("Keycloak config not found!");
            return null;
        }
        return openKeycloak(config);
    }

    @Override
    public Keycloak openKeycloak(SysConfIntegrationKeycloak config) {
        OkAssert.notNull(config, "Invalid configuration!");

        var builder = KeycloakBuilder.builder()
                .serverUrl(config.getServerUrl())
                .username(config.getUsername())
                .password(config.getPassword())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret());

        Keycloak keycloak = builder
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(10).build())
                .build();

        Log.infof("Opened Keycloak %s", keycloak);
        return keycloak;
    }
}
