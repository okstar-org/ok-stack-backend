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

package org.okstar.platform.common.jms;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Topic;
import org.okstar.platform.common.core.utils.OkAssert;

@ApplicationScoped
public class JmsTopicSenderImpl implements JmsTopicSender {

    @Inject
    ConnectionFactory connectionFactory;

    @Override
    public void send(String topicName, String message) {
        Log.debugf("send topicName:%s, message:%s", topicName, message);

        OkAssert.notNull(topicName, "topicName must not be null");
        OkAssert.notNull(message, "message must not be null");

        try (JMSContext context = connectionFactory.createContext()) {
            JMSProducer producer = context.createProducer();
            Topic topic = context.createTopic(topicName);
            producer.send(topic, message);
        }
    }
}
