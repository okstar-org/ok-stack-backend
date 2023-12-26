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
public class SignInResult {
    private String token;
    private String token_type;
    private Long expires_in;
    private String refresh_token;
    private Long refresh_expires_in;
    private String session_state;
}
