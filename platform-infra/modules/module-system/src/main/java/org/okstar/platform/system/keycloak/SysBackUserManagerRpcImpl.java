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

package org.okstar.platform.system.keycloak;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.system.dto.BackUser;
import org.okstar.platform.system.rpc.SysBackUserManagerRpc;

import java.util.Optional;

@ApplicationScoped
public class SysBackUserManagerRpcImpl implements SysBackUserManagerRpc {

    @Inject
    KeycloakUserManager keycloakUserManager;

    @Override
    public BackUser add(BackUser user) {
        return keycloakUserManager.addUser(user);
    }

    @Override
    public boolean delete(String username) {
        return keycloakUserManager.deleteUser(username);
    }

    @Override
    public Optional<BackUser> get(String username) {
        return keycloakUserManager.getUser(username);
    }

    @Override
    public void resetPassword(String username, String password) {
        keycloakUserManager.resetPassword(username, password);
    }

    @Override
    public void forgot(String username) {
        keycloakUserManager.forgot(username);
    }
}
