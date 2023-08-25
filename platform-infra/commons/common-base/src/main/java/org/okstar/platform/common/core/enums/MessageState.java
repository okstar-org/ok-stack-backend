/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
 * OkEDU-Classroom is licensed under Mulan PubL v2.
 * You can use this software according to the terms and conditions of the Mulan
 * PubL v2. You may obtain a copy of Mulan PubL v2 at:
 *          http://license.coscl.org.cn/MulanPubL-2.0
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PubL v2 for more details.
 * /
 */

package org.okstar.platform.common.core.enums;

/**
 * 消息状态
 *
 * 
 */
public enum MessageState {
    /**
     * 未处理
     */
    TODO("1", "未处理"),
    /**
     * 已完成
     */
    FINISHED("2", "已完成"),
    /**
     * 已删除
     */
    DELETED("3", "已删除"),
    /**
     * 已取消
     */
    CANCELED("4", "已取消"),
    /**
     * rabbitMQ消息投递失败，持久化，待手动处理的消息
     */
    FAILED("5", "消息投递失败");

    private final String code;
    private final String info;

    MessageState(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
