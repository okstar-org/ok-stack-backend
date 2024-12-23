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

package org.okstar.platform.system.dto;

import lombok.Data;
import lombok.ToString;

/**
 * IM配置信息
 */
@Data
@ToString
public class SysConfImDTO {
    /**
     * xmpp地址
     */
    private String host;

    /**
     * xmpp IM服务端口
     */
    private int port = 5222;

    /**
     * xmpp服务器管理端口
     */
    private int adminPort = 9090;

    /**
     * xmpp服务器 API SecretKey
     */
    private String apiSecretKey;


}
