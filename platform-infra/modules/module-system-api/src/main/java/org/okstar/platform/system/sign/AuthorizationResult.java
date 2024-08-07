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

package org.okstar.platform.system.sign;

import lombok.Builder;
import lombok.Data;

/**
 * 登录返回信息
 * <p>
 * export interface Token {
 * access_token: string;
 * refresh_token: string;
 * token?: string;
 * token_type?: string;
 * expires_in?: number;
 * }
 */
@Data
@Builder
public class AuthorizationResult {
    private String username;

    //tokenType："Bearer"
    protected String tokenType;
    protected String accessToken;

    /**
     * 过期时间：
     * 以秒为单位或描述的时间跨度字符串表示。
     * 如：60，”2 days”，”10h”，”7d”
     */
    private Long expiresIn;

    //refresh
    private String refreshToken;
    private Long refreshExpiresIn;

    //session
    private String session_state;
}
