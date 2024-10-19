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

package org.okstar.platform.common.security.domain;

import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class OkRealmAccess {
    private Set<String> roles;

    /**
     * realm_access=>{"roles":
     * ["manager","offline_access","rest-all","admin","uma_authorization","kie-server","default-roles-ok-star"]}
     *
     * @return RealmAccess
     */
    public static OkRealmAccess of(JsonObject m) {
        OkRealmAccess realmAccess = new OkRealmAccess();
        var jsonNode = m.getJsonArray("roles");
        if (jsonNode != null) {
            realmAccess.setRoles(jsonNode.stream().map(e -> ((JsonString) e).getString()).collect(Collectors.toSet()));
        }
        return realmAccess;
    }
}
