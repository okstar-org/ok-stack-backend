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

package org.okstar.platform.auth.keycloak;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.constraint.Assert;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.okstar.platform.auth.backend.BackUser;
import org.okstar.platform.auth.backend.BackUserManager;
import org.okstar.platform.common.core.exception.OkRuntimeException;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class KeycloakUserManagerImpl extends KeycloakManagerImpl implements BackUserManager {
    public static final String OKSTAR_REALM = "okstar";

    Keycloak keycloak;

    void startup(@Observes StartupEvent event) {
        // # https://quarkus.io/guides/security-keycloak-admin-client
        String realm = "master";
//        String grantType = "PASSWORD";
        String clientId = "admin-cli";
        String username = "admin";
        String password = "okstar";

        Assert.assertTrue(config.serverUrl.isPresent());

        //处理需要启动执行的任务
        keycloak = Keycloak.getInstance(
                config.serverUrl.get(),
                realm,
                username,
                password,
                clientId,
                null,
                null,
                (new ResteasyClientBuilderImpl().connectionPoolSize(20).build()),
                true, null, null);
    }

    private UsersResource usersResource() {
        RealmResource realm = keycloak.realms().realm(OKSTAR_REALM);
        return realm.users();
    }

    @Override
    public List<BackUser> users() {
        UsersResource usersResource = usersResource();
        return usersResource.list().stream()//
                .map(KeycloakUserManagerImpl::toBackend)//
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BackUser> getUser(String username) {
        Log.infof("getUser:%s", username);
        UsersResource usersResource = usersResource();
        List<UserRepresentation> list = usersResource.search(username);
        return list.stream().map(KeycloakUserManagerImpl::toBackend).toList().stream().findFirst();
    }


    @Override
    public BackUser addUser(BackUser user) {
        Log.infof("Add user:%s", user.getUsername());
        UsersResource usersResource = usersResource();
        try {
            var response = usersResource.create(toRepresent(user));
            Log.infof("statusCode=>%s", response.getStatus());
            Assert.assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
        } catch (Exception e) {
            throw new OkRuntimeException("Creating account exception occurred: %s".formatted(e.getMessage()), e);
        }

        var u = usersResource.search(user.getUsername()).stream().peek(userRepresentation -> {
            Log.infof("Find auth system user:%s=>%s", user.getUsername(), userRepresentation.getId());
            CredentialRepresentation cr = new CredentialRepresentation();
            cr.setUserLabel("My password");
            cr.setType("password");
            cr.setValue(user.getPassword());
            UserResource userResource = usersResource.get(userRepresentation.getId());
            userResource.resetPassword(cr);
        }).findFirst().map(KeycloakUserManagerImpl::toBackend).orElse(null);
        Log.debugf("User is:%s", u);
        return u;
    }

    @Override
    public boolean deleteUser(String username) {
        Log.infof("Delete user:%s", username);
        Optional<BackUser> backUser = getUser(username);
        if (backUser.isEmpty()) {
            Log.warnf("User:%s is not exist", username);
            return true;
        }
        UsersResource usersResource = usersResource();
        var response = usersResource.delete(backUser.get().getId());
        Log.infof("Delete user:%s=>", username, response.getStatus());
        return response.getStatus() == Response.Status.NO_CONTENT.getStatusCode();
    }

    private UserRepresentation toRepresent(BackUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setId(user.getId());
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }

    private static BackUser toBackend(UserRepresentation userRepresentation) {
        BackUser.BackUserBuilder builder = BackUser.builder();
        builder.username(userRepresentation.getUsername());
        builder.firstName(userRepresentation.getFirstName());
        builder.lastName(userRepresentation.getLastName());
        builder.email(userRepresentation.getEmail());
        builder.id(userRepresentation.getId());
        return builder.build();
    }
}
