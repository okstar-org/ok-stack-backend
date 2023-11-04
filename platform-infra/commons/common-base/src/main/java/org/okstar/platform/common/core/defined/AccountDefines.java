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

package org.okstar.platform.common.core.defined;

import lombok.Getter;

/**
 * 帐号相关定义
 */
public interface AccountDefines {

    @Getter
    enum BindType {
        /**
         * 手机号
         * 邮箱
         * 微信
         * QQ
         * 钉钉
         * 飞书
         */
        phone,
        email,
        wx,
        qq,
        dingding,
        feishu
    }

    /**
     * 帐号状态
     */
    @Getter
    enum Status {
        NONE,
        OK,
        DISABLED,
    }
}
