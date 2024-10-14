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

import io.quarkus.logging.Log;
import io.smallrye.common.constraint.Assert;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.exception.ResteasyBadRequestException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.*;
import org.okstar.platform.common.exception.OkRuntimeException;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.system.dto.BackUser;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class BackUserManagerImpl implements BackUserManager {

    @Inject
    KeycloakService keycloakService;
    @Inject
    BackRoleManager backRoleManager;

    @Override
    public void assignRole(String username, String roleId){
        throw new UnsupportedOperationException();
    }

    @Override
    public void unassignRole(String username, String roleId){
        throw new UnsupportedOperationException();
    }

    /**
     * get user roles
     *
     * @param username
     * @return
     */
    @Override
    public List<BackRoleDTO> listRoles(String username) {
        Log.infof("Get user role for user:%s", username);

        Optional<BackUser> backUser = getUser(username);
        if (backUser.isEmpty()) {
            Log.warnf("User:%s is not exist", username);
            return List.of();
        }

        List<BackRoleDTO> list = new ArrayList<>();
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            UsersResource usersResource = keycloakService.getUsersResource(keycloak);
            UserResource userResource = usersResource.get(backUser.get().getId());
            RoleMappingResource roles = userResource.roles();
            if (roles == null) {
                return List.of();
            }

            MappingsRepresentation all = roles.getAll();
            Map<String, ClientMappingsRepresentation> clientMappings = all.getClientMappings();
            if (clientMappings != null) {
                clientMappings.forEach((c, k) -> {
                    Log.debugf("%s=>%s", c, k);
                    List<BackRoleDTO> dtos = k.getMappings().stream().map(backRoleManager::toDTO).toList();
                    list.addAll(dtos);
                });
            }

            List<RoleRepresentation> realmRoles = all.getRealmMappings();
            if (realmRoles != null) {
                realmRoles.forEach((k) -> {
                    Log.debugf("realm role=>%s", k);
                    list.add(backRoleManager.toDTO(k));
                });
            }
        }

        return list;
    }


    /**
     * 重置密码:
     * https://www.keycloak.org/docs/latest/server_development/#modifying-forgot-passwordcredential-flow
     *
     * @param username
     * @param password
     */
    @Override
    public void resetPassword(String username, String password) {
        Log.infof("Reset password for user:%s", username);
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            UsersResource usersResource = keycloakService.getUsersResource(keycloak);
            setPassword(usersResource, username, password);
        }
    }

    @Override
    public void forgot(String username) {
        try (Keycloak keycloak = keycloakService.openKeycloak()) {

            UsersResource usersResource = keycloakService.getUsersResource(keycloak);
            List<UserRepresentation> list = usersResource.search(username);
            OkAssert.isTrue(!list.isEmpty(), "用户不存在！");
            try {
                for (UserRepresentation user : list) {
                    UserResource userResource = usersResource.get(user.getId());
                    userResource.resetPasswordEmail();
                }
            } catch (Exception e) {
                Log.errorf(e, "重置密码异常！");
                if (e instanceof ResteasyBadRequestException) {
                    BadRequestException unwrap = ((ResteasyBadRequestException) e).unwrap();
                    var entity = unwrap.getResponse().readEntity(JsonObject.class);
                    throw new OkRuntimeException(entity.getString("errorMessage"));
                }
                throw new OkRuntimeException("服务器异常！");
            }
        }
    }

    @Override
    public List<BackUser> users() {
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            UsersResource usersResource = keycloakService.getUsersResource(keycloak);
            return usersResource.list().stream()//
                    .map(BackUserManagerImpl::toBackend)//
                    .collect(Collectors.toList());
        }
    }


    /**
     * get user
     *
     * @param username
     * @return
     */
    @Override
    public Optional<BackUser> getUser(String username) {
        Log.infof("getUser:%s", username);
        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            UsersResource usersResource = keycloakService.getUsersResource(keycloak);
            List<UserRepresentation> list = usersResource.search(username);
            return list.stream().map(BackUserManagerImpl::toBackend).toList().stream().findFirst();
        }
    }


    @Override
    public BackUser addUser(BackUser user) {
        Log.infof("Add user:%s", user);

        OkAssert.hasText(user.getUsername(), "username is required");
        OkAssert.hasText(user.getPassword(), "password is required");

        try (Keycloak keycloak = keycloakService.openKeycloak()) {
            UsersResource usersResource = keycloakService.getUsersResource(keycloak);
            try {
                var response = usersResource.create(toRepresent(user));
                Log.infof("statusCode=>%s", response.getStatus());
                Assert.assertTrue(response.getStatus() == Response.Status.CREATED.getStatusCode());
            } catch (Exception e) {
                throw new OkRuntimeException("Creating account exception occurred: %s".formatted(e.getMessage()), e);
            }

            var u = usersResource.search(user.getUsername()).stream().peek(userRepresentation -> {
                CredentialRepresentation cr = new CredentialRepresentation();
                cr.setUserLabel("My password");
                cr.setType("password");
                cr.setValue(user.getPassword());
                UserResource userResource = usersResource.get(userRepresentation.getId());
                userResource.resetPassword(cr);
            }).findFirst().map(BackUserManagerImpl::toBackend).orElse(null);

            Log.infof("User is:%s", u);
            return u;
        }
    }

    @Override
    public boolean deleteUser(String username) {
        Log.infof("Delete user:%s", username);
        try (Keycloak keycloak = keycloakService.openKeycloak()) {

            Optional<BackUser> backUser = getUser(username);
            if (backUser.isEmpty()) {
                Log.warnf("User:%s is not exist", username);
                return true;
            }
            UsersResource usersResource = keycloakService.getUsersResource(keycloak);
            var response = usersResource.delete(backUser.get().getId());
            Log.infof("Delete user:%s=>", username, response.getStatus());
            return response.getStatus() == Response.Status.NO_CONTENT.getStatusCode();
        }
    }

    private void setPassword(UsersResource usersResource, String username, String password) {
        List<UserRepresentation> list = usersResource.search(username);
        OkAssert.isTrue(!list.isEmpty(), "帐号不正确！");
        list.forEach(userRepresentation -> {
            CredentialRepresentation cr = new CredentialRepresentation();
            cr.setUserLabel("My password");
            cr.setType("password");
            cr.setValue(password);
            UserResource userResource = usersResource.get(userRepresentation.getId());
            userResource.resetPassword(cr);
        });
    }

    private UserRepresentation toRepresent(BackUser user) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setFirstName(user.getFirstName());
        userRepresentation.setLastName(user.getLastName());
        userRepresentation.setEmail(user.getEmail());
        userRepresentation.setId(user.getId());
        userRepresentation.setAttributes(user.getAttributes());
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
