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
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.okstar.platform.auth.backend.BackUser;
import org.okstar.platform.auth.backend.BackUserManager;
import org.springframework.util.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class KeycloakUserManagerImpl extends KeycloakManagerImpl implements BackUserManager {

    private UsersResource usersResource() {
        RealmResource realm = keycloak.realm(realmName);

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
    public String addUser(BackUser user) {
        Log.infof("addUser:%s", user.getUsername());

        UsersResource usersResource = usersResource();
        try (Response response = usersResource.create(toRepresent(user))) {
            Log.infof("statusCode=>%s", response.getStatus());
            Assert.isTrue(response.getStatus() == Response.Status.CREATED.getStatusCode(),
                    "添加失败");
        }

        usersResource.search(user.getUsername()).forEach(ur -> {
            Log.infof("user:%s=>%s", user.getUsername(), ur.getId());
            CredentialRepresentation cr = new CredentialRepresentation();
            cr.setUserLabel("My password");
            cr.setType("password");
            cr.setValue(user.getPassword());
            UserResource userResource = usersResource.get(ur.getId());
            userResource.resetPassword(cr);
        });
        return user.getUsername();
    }

    private UserRepresentation toRepresent(BackUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setId(user.getId());
        return userRepresentation;
    }

    private static BackUser toBackend(UserRepresentation userRepresentation) {
        BackUser.BackUserBuilder builder = BackUser.builder();
        builder.username(userRepresentation.getUsername());
        builder.firstName(userRepresentation.getFirstName());
        builder.lastName(userRepresentation.getLastName());
        builder.email(userRepresentation.getEmail());
        return builder.build();
    }
}
