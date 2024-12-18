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

package org.okstar.platform.bus.server;

import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

/**
 * 总线服务，负责整个分布式消息交互通讯
 */
@Startup
@Singleton
public class BusServer {

    public void init(@Observes StartupEvent event) throws Exception {
        Log.info("Init busServer...");
        EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();
        embedded.start();
        Log.info("Initialize busServer successfully.");
    }
}