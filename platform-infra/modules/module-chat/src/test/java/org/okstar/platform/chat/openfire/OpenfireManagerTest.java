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

package org.okstar.platform.chat.openfire;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.UserEntities;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OpenfireManagerTest {

    @Inject
    OpenfireManager openfireManager;

    @Test
    void makeXmppClient() {
        String secret="0SvdwrDYAl3Pnzrz";
        RestApiClient client = openfireManager.makeXmppClient("localhost", 9090, secret);
        UserEntities users = client.getUsers();
        users.getUsers().forEach(user->{
            Log.infof("user=%s", user);
        });
    }
}