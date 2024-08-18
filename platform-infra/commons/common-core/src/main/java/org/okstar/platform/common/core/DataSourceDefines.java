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

package org.okstar.platform.common.core;

/**
 * 数据源相关定义
 */
public interface DataSourceDefines {

    /**
     * 数据源类型
     */
    enum Type {
        db,   //数据库
        ai,     //ai接口
        api,    //API
        saas,   //saas集成
    }

    /**
     * DB类型
     */
    enum DatabaseType {
        mysql,
        pgsql,
        oracle,
        sqlserver,
        redis,
        mongo
    }
}
