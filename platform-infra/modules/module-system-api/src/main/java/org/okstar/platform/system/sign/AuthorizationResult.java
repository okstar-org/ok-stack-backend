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
 * 成功认证的返回信息
 */
@Data
@Builder
public class AuthorizationResult {
    private String username;

    /**
     * tokenType："Bearer"
     */
    protected String tokenType;

    /**
     * access token
     */
    protected String accessToken;

    /**
     * 过期时间：
     * 以秒为单位或描述的时间跨度字符串表示。
     * 如：60，”2 days”，”10h”，”7d”
     */
    private Long expiresIn;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 刷新token过期时间
     */
    private Long refreshExpiresIn;

    /**
     * <pre>
     * session_state 是OpenID Connect（OIDC）协议中的一个参数，是一个重要的安全机制，
     * 用于在认证流程中帮助检测和防止会话固定攻击（session fixation attacks）。
     * 是一个可选的、不透明的值，由认证服务器生成并返回给客户端（通常是依赖方Relying Party, RP）,
     * 它通过提供一个会话期间唯一的标识符来帮助增强认证流程的安全性。<b>这个值在用户的认证会话期间是唯一的，并且每次用户登录时都会改变。</b>
     * </pre>
     *
     * <p>
     * 它的主要作用是：
     * </p>
     * <ol>
     *     <li>会话状态验证：客户端可以使用session_state来检测用户的会话状态是否发生了变化。 如果客户端检测到session_state的值与上一次认证响应中的不同，这可能意味着用户的会话已经被重新认证或存在其他需要注意的状态变化。</li>
     *     <li>防止会话固定攻击：通过确保每次认证返回的session_state都是唯一的， 如果攻击者无法预测或控制session_state的值，那么他们就很难利用预先设置的会话</li>
     *     <li>会话管理：session_state还可以用于客户端内部的会话管理，帮助客户端跟踪和管理用户的认证状态。</li>
     * </ol>
     *
     * @link <a href="https://openid.net/specs/openid-connect-session-1_0.html#CreatingUpdatingSessions">CreatingUpdatingSessions</a>
     */
    private String session_state;
}
