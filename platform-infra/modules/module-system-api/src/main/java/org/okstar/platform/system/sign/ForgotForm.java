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
 * 忘记密码表单
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgotForm extends Req {

    //帐号类型
    AccountDefines.BindType accountType;

    //国家代号
    String iso;

    //手机号或者邮箱
    String account;

}
