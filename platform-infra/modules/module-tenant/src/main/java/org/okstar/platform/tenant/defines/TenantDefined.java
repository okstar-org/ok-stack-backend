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

package org.okstar.platform.tenant.defines;

public interface TenantDefined {
    /**
     * 租户状态
     * 状态（创建、启动、停止、销毁）
     */
    enum TenantStatus {
        Created,
        Started,
        Stopped,
        Destroyed,
        Error
    }

    enum DataBaseType{
        pgsql,
        mysql
    }

    enum RunOn {
        docker,
        os
    }
}