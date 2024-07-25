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

package org.okstar.platform.org.bus;

import io.quarkus.arc.Arc;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.jms.*;
import org.okstar.platform.common.datasource.OkAbsService;

import java.util.function.Consumer;

@Startup
@Singleton
public class ConsumerJms extends OkAbsService {

    @Inject
    ConnectionFactory connectionFactory;

//    public void init(@Observes StartupEvent event) throws Exception {
//        JMSContext context = connectionFactory.createContext();
//        JMSConsumer jmsConsumer = context.createConsumer(context.createTopic("ModuleSystemApplication.SysProfile.UPDATE"));
//        Arc.container().getExecutorService().execute(() -> {
//            do {
//                Message message = jmsConsumer.receive();
//                try {
//                    String body = message.getBody(String.class);
//                    Log.infof("message=%s", body);
//                } catch (JMSException e) {
//                    Log.warnf(e, "Unable to parse message:%s", message);
//                }
//            } while (true);
//        });
//    }

    public boolean setTopicListener(String topicName, Consumer<Message> consumer) {
        Log.debugf("Set topic listener: %s", topicName);
        JMSContext context;
        try {
            //不用关闭context，避免内部线程无法读取
            context = connectionFactory.createContext();
        } catch (JMSRuntimeException e) {
            Log.warnf("Create JMS context failed: %s", e.getMessage());
            return false;
        }

        JMSConsumer jmsConsumer = context.createConsumer(context.createTopic(topicName));
        Arc.container().getExecutorService().execute(() -> {
            do {
                Message message = jmsConsumer.receive();
                consumer.accept(message);
            } while (true);
        });

        return true;
    }
}
