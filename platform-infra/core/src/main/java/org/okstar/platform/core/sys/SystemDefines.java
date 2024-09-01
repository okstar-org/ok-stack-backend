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

package org.okstar.platform.core.sys;

import lombok.Getter;

/**
 * 系统相关定义
 */
public interface SystemDefines {

    /**
     * 数据状态
     */
    @Getter
    enum DataFlag {
        OK, //正常
        DELETED,//删除
    }

    @Getter
    enum Endpoint {
        PC, //电脑
        MOBILE,//移动
        PAD//平板
    }



    /**
     * 消息类型
     *
     *
     */
    enum MessageType {
        Info,   //信息
        Warn,   //警告
        Error,  //错误
        Task,   //任务
    }

    enum MessageTaskStatus{
        Created,//已创建
        Processing,//处理中
        Processed,//已处理
        Timeout,//超时
    }

}
