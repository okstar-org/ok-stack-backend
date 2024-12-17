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

package org.okstar.platform.common.security.domain;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * 登录用户信息
 */
@Data
public class OkLogonUser implements Serializable
{
    /**
     * jwt typ
     */
    private String type;

    /**
     * jwt id
     */
    private String id;

    /**
     * session id
     */
    private String sid;

    /**
     * 用户名
     */
    private String username;
    private String nickName;

    private String familyName;
    private String givenName;
    private String middleName;

    private String clientId;

    private String email;

    private Boolean emailVerified;

    private String scope;


    /**
     * expiration
     */
    private Instant exp;

    /**
     * issuedAt
     */
    private Instant iat;

    /**
     * 权限列表
     */
    private OkAuthorization authorization;

    /**
     * 角色列表
     */
    private OkRealmAccess realmAccess;

    /**
     * 资源访问
     */
    private OkResourceAccess resourceAccess;

    /**
     * subject
     */
    private String subject;

    /**
     * token
     */
    private String token;

}
