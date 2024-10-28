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

import lombok.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 后端认证用户(keycloak)
 */
@ToString
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackUser {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled;

    private Map<String, List<String>> attributes = new LinkedHashMap<>();

    public String getPhone() {
        return attributes.get("phone").stream().findFirst().orElse(null);
    }

    public void setPhone(String phone) {
        if (phone != null && !phone.isEmpty()) {
            this.attributes.put("phone", List.of(phone));
        }
    }

    public void setNickname(String nickname) {
        if (nickname != null && !nickname.isEmpty()) {
            this.attributes.put("nickname", List.of(nickname));
        }
    }

    public String getNickname() {
        return attributes.get("nickname").stream().findFirst().orElse(null);
    }
}
