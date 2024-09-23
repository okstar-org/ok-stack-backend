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

import io.quarkus.logging.Log;
import io.quarkus.oidc.client.Tokens;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.auth.keycloak.AuthzClientManager;
import org.okstar.platform.system.sign.AuthorizationResult;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class AuthzClientManagerTest {

    @Inject
    AuthzClientManager authzClientManager;

    String username = "412555203@qq.com";
    String password = "123456";

    private AuthorizationResult auth(String username, String password) {
        AuthorizationResult result = authzClientManager.authorization(username, password);
        Log.infof("result=%s", result);
        return result;
    }

    private Tokens auth2() {
        Tokens result = authzClientManager.getAccessToken();
        Log.infof("result=%s", result);
        return result;
    }

    @Test
    void getAccessToken() {
        Tokens result = auth2();

    }

    @Test
    void authorization() {
        AuthorizationResult result = auth(username, password);
        assertNotNull(result);
    }


    @Test
    void refresh() {
        AuthorizationResult auth = auth(username, password);
        AuthorizationResult result = authzClientManager.refresh(auth.getRefreshToken());
        Log.infof("result=%s", result.getAccessToken());
        assertNotNull(result.getAccessToken());
    }

    @Test
    void revoke() {
        AuthorizationResult auth = auth(username, password);
        Boolean revoke = authzClientManager.revoke(auth.getAccessToken());
        assertTrue(revoke);
    }
}