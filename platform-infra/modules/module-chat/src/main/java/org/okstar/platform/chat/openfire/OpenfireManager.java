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

package org.okstar.platform.chat.openfire;

import io.quarkus.arc.Arc;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import lombok.Getter;
import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;
import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;
import org.igniterealtime.restclient.enums.SupportedMediaType;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.concurrent.ExecutorService;

@Getter
@ApplicationScoped
public class OpenfireManager extends Thread {
    //    @ConfigProperty(name = "chat.auth.secret-key")
    private String authSecretKey = "JxHQUSsqDLJbArL7";

    private RestApiClient restApiClient;


    public void init(@Observes StartupEvent e) {
        setDaemon(true);
        setName("OpenfireManager");

        // Shared secret key
        AuthenticationToken token = new AuthenticationToken(authSecretKey);
        restApiClient = new RestApiClient("http://meet.chuanshaninfo.com", 9090, //
                token, SupportedMediaType.JSON);

        ExecutorService executorService = Arc.container().getExecutorService();
        executorService.submit(this);
    }

    @Override
    public void run() {


    }

    public UserEntities users(){
        UserEntities users = restApiClient.getUsers();
        Log.infof("users:%s", users);
        return users;
    }

    public UserEntity findUserByUsername(String username){
        Log.infof("findUserByUsername:%s", username);
        UserEntity user = restApiClient.getUser(username);
        Log.infof("user:%s", user);
        return user;
    }

}
