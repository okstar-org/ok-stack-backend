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

package org.okstar.platform.org.connect.domain;


import lombok.Data;
import org.okstar.platform.org.connect.ConnectorDefines;

/**
 * 组织-集成配置
 */
@Data
public class OrgIntegrateConf {

    /**
     * 应用类型
     */
    private ConnectorDefines.Type type;

    //应用ID
    private String appId;
    
    //应用名称
    private String name;

    /**
     * 应用凭证(key)
     */
    
    private String certKey;

    /**
     * 应用凭证(secret)
     *
     */
    private String certSecret;

    /**
     * 基础URL
     */
    private String baseUrl;

    /**
     * 跟部门ID
     */
    private String rootDeptId;

    /**
     * 同步周期（分钟）
     */
    private int period;
}
