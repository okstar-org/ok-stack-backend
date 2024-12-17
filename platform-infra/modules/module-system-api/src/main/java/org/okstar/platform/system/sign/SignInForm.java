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

import lombok.*;
import org.okstar.platform.common.web.bean.Req;
import org.okstar.platform.core.account.AccountDefines;

/**
 * 登录授权实体
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm extends Req {

    /**
     * account  :"18510248810"
     * grantType:"password"
     * password : "123456"
     * remember_me: true
     */
    private AccountDefines.BindType type = AccountDefines.BindType.email;
    private AccountDefines.DeviceType deviceType = AccountDefines.DeviceType.PC;
    private String iso = AccountDefines.DefaultISO;
    /**
     * 授权类型（password:密码）
     */
    private String grantType;
    /**
     * 帐号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否记住
     */
    private Boolean rememberMe;
}
