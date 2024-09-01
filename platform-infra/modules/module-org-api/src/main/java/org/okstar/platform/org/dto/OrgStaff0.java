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

package org.okstar.platform.org.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.okstar.platform.core.user.UserDefines;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织成员
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgStaff0 {

    //account
    private String username;
    private Long accountId;

    private Long id;
    private String no;
    private String name;
    private String phone;
    private String email;
    private UserDefines.Gender gender;
    private Date birthday;
    private Date joinedDate;

    private List<OrgPost0> posts;

    public String getPostNames() {
        return posts == null ? "" : posts.stream().map(OrgPost0::getName).collect(Collectors.joining(","));
    }
}
