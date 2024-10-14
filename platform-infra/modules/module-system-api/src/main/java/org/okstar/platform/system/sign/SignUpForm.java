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
import org.okstar.platform.common.string.OkNameUtil;
import org.okstar.platform.core.account.AccountDefines;

/**
 * 注册实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SignUpForm extends Req {

    //帐号类型
    private AccountDefines.BindType accountType;

    //国家代号
    private String iso;

    //语言
    private String language;

    //手机号或者邮箱
    private String account;

    //密码
    private String password;

    //姓
    private String firstName;

    //名
    private String lastName;

    //昵称
    private String nickname;

    //头像
    private String avatar;

    public String getName() {
        return OkNameUtil.combinePeopleName(language, firstName, lastName);
    }
}
