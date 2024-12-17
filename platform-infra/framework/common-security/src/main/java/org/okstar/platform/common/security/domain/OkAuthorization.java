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

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OkAuthorization {

    private Set<OkPermission> permissions;

    /**
     * 从Json解析授权信息
     * @param m 来自keycloak授权数据
     * @return
     */
    public static OkAuthorization of(JsonObject m) {
        /**
         * authorization=>{ "permissions":[
         *             // {"rsid":"4ea6f7e4-1c1b-43aa-888a-64e9f70c71e4","rsname":"OrgStaff"},
         *             // {"rsid":"e3ea9aea-0cdc-4c26-a10e-c2e8f7b2b4a0","rsname":"Org"},
         *             // {"rsid":"7de49e5f-f0d9-4566-aa99-09f489b1d1cd","rsname":"OrgDepartment"},
         *             // {"rsid":"b1472701-5186-41a6-b890-a6a61b85dfcf","rsname":"Default Resource"},
         *             // {"rsid":"2ef1958d-b8b0-4471-b62c-e36d4fddab60","rsname":"OrgIntegration"}]}
         */
        OkAuthorization o = new OkAuthorization();
        JsonArray array = m.getJsonArray("permissions");
        Set<OkPermission> set = array.stream().map(e -> {
            JsonObject object = e.asJsonObject();
            return OkPermission.builder()
                    .rsid(object.getString("rsid")) //资源ID
                    .rsname(object.getString("rsname")) //资源名称
                    .build();
        }).collect(Collectors.toSet());
        o.setPermissions(set);
        return o;

    }
}
