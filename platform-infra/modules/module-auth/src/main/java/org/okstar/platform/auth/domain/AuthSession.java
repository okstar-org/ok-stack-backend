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

package org.okstar.platform.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.okstar.platform.common.datasource.domain.OkEntity;
import org.okstar.platform.core.account.AccountDefines;

/**
 * 认证Session实体
 * <br>
 * 一个用户成功登录产生一个 `AuthSession`
 */
@Getter
@Setter
@Entity
public class AuthSession extends OkEntity {

    /**
     * 帐号
     */
    private String username;

    @Enumerated(EnumType.STRING)
    private AccountDefines.DeviceType deviceType;

    @Enumerated(EnumType.STRING)
    private AccountDefines.BindType loginType;

    @Enumerated(EnumType.STRING)
    private AccountDefines.GrantType grantType;

    @Lob
    @Column(length = Short.MAX_VALUE)
    private String accessToken;
    private Long expiresIn;

    @Lob
    @Column(length = Short.MAX_VALUE)
    private String refreshToken;
    private Long refreshExpiresIn;

    private String sessionState;

}
