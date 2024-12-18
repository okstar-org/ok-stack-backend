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

package org.okstar.platform.auth.dto;

import lombok.Builder;
import lombok.Data;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.dto.SysAccountDTO;

/**
 * 自己
 */
@Data
@Builder
public class Me {
    //帐号信息
    SysAccountDTO account;

    /**
     * 用户显示名称
     * 优先级：昵称》姓名》用户名
     */
    public String getDisplayName() {
        String nickname = account.getNickname();
        if (OkStringUtil.isNoneBlank(nickname)) {
            //昵称
            return nickname;
        }
        return account.getUsername();
    }
}
