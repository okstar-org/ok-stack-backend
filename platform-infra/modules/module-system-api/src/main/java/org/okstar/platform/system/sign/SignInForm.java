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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.web.bean.Req;

/**
 * 注册实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInForm extends Req {

    /**
     * account
     * :
     * "18510248810"
     * grantType
     * :
     * "password"
     * password
     * :
     * "123456"
     * remember_me
     * :
     * true
     */

    private AccountDefines.BindType accountType = AccountDefines.BindType.phone;
    private String grantType;
    private String account;
    private String password;
    private Boolean rememberMe;
}
