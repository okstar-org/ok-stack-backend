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

package org.okstar.platform.system.conf.startup;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.okstar.platform.system.conf.service.SysKeycloakService;
import org.okstar.platform.system.conf.domain.SysConfIntegrationKeycloak;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Keycloak启动服务
 */
@ApplicationScoped
public class KeycloakStartup {


    @Inject
    SysKeycloakService keycloakService;
    @Inject
    ExecutorService executorService;

    /**
     * 初始化Keycloak相关设置
     * @param event
     */
    void startup(@Observes StartupEvent event) {
        Log.infof("startup ...");

        SysConfIntegrationKeycloak conf = keycloakService.initConfig();
        executorService.execute(() -> {
            String value = null;
            do {
                String realm = keycloakService.getRealm();
                Log.infof("Initializing realm: %s", realm);

                try {
                    value = keycloakService.initRealm(conf, realm);
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

}
