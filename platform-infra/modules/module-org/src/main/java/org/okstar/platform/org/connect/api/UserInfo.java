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

package org.okstar.platform.org.connect.api;

import lombok.*;
import org.okstar.platform.core.user.UserDefines;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String id;
    private String name;
    private String nickname;
    private String unionId;
    private Boolean isActive;
    private Boolean isBoos;
    private Boolean isLeader;
    private Boolean isAdmin;
    private String mobilePhone;
    private String linePhone;
    private String avatar;
    private String email;
    private String orgMail;
    private String title;
    private UserDefines.Gender gender;
}
