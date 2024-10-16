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

package org.okstar.platform.system.conf;

/**
 * 配置group、prefix相关定义
 * 不能重复否则出现替代或者覆盖问题
 */
public interface SysConfDefines {

    /**
     * 个人配置组
     */
    String CONF_GROUP_PERSONAL_PREFIX = "sys.conf.personal";

    /**
     * 集成配置组
     */
    String CONF_GROUP_INTEGRATION_PREFIX = "sys.conf.integration";

    /**
     * 系统设置
     */
    String CONF_GROUP_SETTINGS_PREFIX = "sys.conf.settings.website";
}
