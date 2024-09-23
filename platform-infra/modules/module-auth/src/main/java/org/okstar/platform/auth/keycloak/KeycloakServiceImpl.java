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

package org.okstar.platform.auth.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.rpc.SysKeycloakConfDTO;
import org.okstar.platform.system.rpc.SysKeycloakRpc;

import java.util.List;

@Transactional
@ApplicationScoped
public class KeycloakServiceImpl implements KeycloakService {

    private ResteasyClient client;

    @Inject
    ObjectMapper objectMapper;
    @Inject
    @RestClient
    SysKeycloakRpc sysKeycloakRpc;

    @Override
    public AuthorizationResource getAuthorizationResource(Keycloak keycloak) {
        SysKeycloakConfDTO config = getStackConfig();
        String realm = config.getRealm();
        String clientId = config.getClientId();


        RealmResource realmResource = keycloak.realms().realm(realm);
        ClientsResource clientsResource = realmResource.clients();
        List<ClientRepresentation> list = clientsResource.findByClientId(clientId);
        if (list.isEmpty()) {
            return null;
        }

        ClientResource clientResource = realmResource.clients().get(list.get(0).getId());
        return clientResource.authorization();
    }

    @Override
    public RolesResource getRoleResource(Keycloak keycloak) {
        RealmResource realm = keycloak.realms().realm(getStackConfig().getRealm());
        return realm.roles();
    }

    @Override
    public UsersResource getUsersResource(Keycloak keycloak) {
        RealmResource realm = keycloak.realms().realm(getStackConfig().getRealm());
        return realm.users();
    }


    @Override
    public ClientRepresentation getClient(String realm, String clientId) {
        Log.debugf("Get client realm: %s clientId: %s", realm, clientId);
        try (Keycloak kc = openKeycloak()) {
            RealmResource realmResource = kc.realms().realm(realm);
            ClientsResource clients = realmResource.clients();
            List<ClientRepresentation> all = clients.findByClientId(clientId);
            if (all.isEmpty()) {
                Log.warnf("Client is no exising!");
                return null;
            }
            return all.get(0);
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
    public Keycloak openKeycloak() {
        SysKeycloakConfDTO config = getAdminConfig();
        if (config == null) {
            Log.warn("Keycloak config not found!");
            return null;
        }
        return openKeycloak(config);
    }


    @Override
    public SysKeycloakConfDTO getAdminConfig() {
        return sysKeycloakRpc.getAdminConf();
    }

    @Override
    public SysKeycloakConfDTO getStackConfig() {
        return sysKeycloakRpc.getStackConf();
    }

    @Override
    public Keycloak openKeycloak(SysKeycloakConfDTO config) {
        OkAssert.notNull(config, "Invalid configuration!");
        Log.infof("Opening keycloak:%s", config.getRealm());

        var builder = KeycloakBuilder.builder()
                .grantType(config.getGrantType())
                .serverUrl(config.getServerUrl())
                .username(config.getUsername())
                .password(config.getPassword())
                .realm(config.getRealm())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret());

        Keycloak keycloak = builder
                .resteasyClient(new ResteasyClientBuilderImpl().connectionPoolSize(1).build())
                .build();

        Log.infof("Opened keycloak: %s", keycloak);
        return keycloak;
    }

}
