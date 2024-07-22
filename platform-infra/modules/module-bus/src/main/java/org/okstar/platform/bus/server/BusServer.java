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
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

@ApplicationScoped
public class BusServer  {

    public void init(@Observes StartupEvent event) throws Exception {

            Log.infof("init busServer...");

            Configuration config = new ConfigurationImpl();
            config.addAcceptorConfiguration("in-vm", "vm://0");
            config.addAcceptorConfiguration("tcp", "tcp://127.0.0.1:5672");

            EmbeddedActiveMQ embedded = new EmbeddedActiveMQ();
            embedded.start();

            Log.infof("Initialize busServer successfully.");

        }
    }


}
