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

package org.okstar.platform.common.core.constant;

/**
 * 消息队列常量信息
 *
 * 
 */
public class RabbitMQConstants {

    public interface Redis {
        String OK = "OK";
        Integer EXPIRE_TIME_MINUTE = 60;// 过期时间, 60s, 一分钟
        Integer EXPIRE_TIME_HOUR = 60 * 60;// 过期时间, 一小时
        Integer EXPIRE_TIME_DAY = 60 * 60 * 24;// 过期时间, 一天
        String TOKEN_PREFIX = "token:";
        String MSG_CONSUMER_PREFIX = "consumer:";
        String ACCESS_LIMIT_PREFIX = "accessLimit:";
    }


    public interface MsgLogStatus {
        Integer DELIVERING = 0;// 消息投递中
        Integer DELIVER_SUCCESS = 1;// 投递成功
        Integer DELIVER_FAIL = 2;// 投递失败
        Integer CONSUMED_SUCCESS = 3;// 已消费
    }


    public interface MailQueue {
        //邮件队列
        String MAIL_ROUTING_KEY = "mail_routing.key";
        String MAIL_EXCHANGE = "mail_exchange";
        String MAIL_QUEUE = "mail_queue";
    }

    public interface OrderQueue {
        //订单队列
        String ORDER_ROUTE_KEY = "order_route_key";
        String ORDER_EXCHANGE = "order_exchange";
        String ORDER_QUEUE = "order_queue";
    }

    public interface DeadQueue {
        //死信队列
        String DEAD_ROUTE_KEY = "dead_route_key";
        String DEAD_EXCHANGE = "dead_exchange";
        String DEAD_QUEUE = "dead_queue";
    }

    public interface DelayQueue {
        //延时队列
        String DELAY_ROUTE_KEY = "delay_route_key";
        String DELAY_EXCHANGE = "delay_exchange";
        String DELAY_QUEUE = "delay_queue";
    }

}
