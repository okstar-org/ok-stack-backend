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

package org.okstar.platform.core.account;

import lombok.Getter;

import java.util.Locale;

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
        ENABLED,
        DISABLED,
    }

    /**
     * 默认国家
     */
    String DefaultISO = Locale.CHINA.getCountry();

    /**
     * 默认语言
     */
    String DefaultLanguage = Locale.CHINA.getLanguage();

    /**
     * 默认密码
     */
    String DefaultPWD = "okstar.123456#";

    /**
     * 默认头像地址（对应前端存在的图片）
     */
    String DefaultAvatar = "/assets/images/avatar.jpg";

    /**
     * 登录设备类型
     */
    enum DeviceType {
        PC,
        Mobile,
    }

}
