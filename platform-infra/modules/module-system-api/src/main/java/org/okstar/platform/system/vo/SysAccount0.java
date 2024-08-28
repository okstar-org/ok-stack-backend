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

package org.okstar.platform.system.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.web.bean.DTO;

import java.util.Optional;

/**
 * 简单帐号对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysAccount0 extends DTO {
    private Long id;
    //国家
    private String iso;
    //语言
    private String lang;

    private String username;

    private String nickname;

    private String avatar;

    public String getAvatar() {
        return Optional.ofNullable(avatar).orElse(AccountDefines.DefaultAvatar);
    }

}
