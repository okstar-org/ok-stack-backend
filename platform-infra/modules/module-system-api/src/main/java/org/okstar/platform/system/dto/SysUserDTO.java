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

package org.okstar.platform.system.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.okstar.platform.core.user.UserDefines;

/**
 * 用户信息（继承SysAccount，包含SysProfile）
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserDTO extends SysAccountDTO {

    /**
     * 性
     */
    private String firstName;

    /**
     * 名
     */
    private String lastName;

    /**
     * 性别
     */
    private UserDefines.Gender gender;

    /**
     * 邮箱
     */
    private String email;

}
