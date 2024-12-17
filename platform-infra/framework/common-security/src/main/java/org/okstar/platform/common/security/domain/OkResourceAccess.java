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
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源
 */
@Data
public class OkResourceAccess {

    /**
     * Client与域相关
     */
    private Map<String, OkRealmAccess> accessMap;

    /**
     * resource_access=>{
     * "ok-stack":{"roles":["HR"]},
     * "ok-bpm":{"roles":["manager","kie-server"]},
     * "account":{"roles":
     * ["manage-account","manage-account-links","view-profile"]}}
     * @return RealmAccess
     */
    public static OkResourceAccess of(JsonObject m) {
        OkResourceAccess resourceAccess = new OkResourceAccess();
        resourceAccess.setAccessMap(new HashMap<>());
        for (String client : m.keySet()) {
            OkRealmAccess realmAccess = OkRealmAccess.of(m.getJsonObject(client));
            resourceAccess.getAccessMap().put(client, realmAccess);
        }
        return resourceAccess;
    }
}
