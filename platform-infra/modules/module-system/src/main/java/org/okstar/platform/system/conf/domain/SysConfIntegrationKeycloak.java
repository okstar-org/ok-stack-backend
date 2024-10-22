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

package org.okstar.platform.system.conf.domain;

import lombok.Data;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.conf.SysConfDefines;
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统管理-集成设置-IM设置
 */
@Data
public class SysConfIntegrationKeycloak implements SysConfItem {


    private String serverUrl;
    private String realm;
    private String clientId;
    private String username;
    private String password;
    private String clientSecret;

    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_INTEGRATION_PREFIX+".keycloak";
    }

    @Override
    public void addProperty(SysPropertyDTO property) {

        if (OkStringUtil.equals(property.getK(), "server-url")) {
            this.serverUrl = property.getV();
        } else if (OkStringUtil.equals(property.getK(), "realm")) {
            this.realm = property.getV();
        } else if (OkStringUtil.equals(property.getK(), "client-id")) {
            this.clientId = property.getV();
        } else if (OkStringUtil.equals(property.getK(), "username")) {
            this.username = property.getV();
        } else if (OkStringUtil.equals(property.getK(), "password")) {
            this.password = property.getV();
        } else if (OkStringUtil.equals(property.getK(), "client-secret")) {
            this.clientSecret = property.getV();
        }
    }

    @Override
    public List<SysPropertyDTO> getProperties() {
        List<SysPropertyDTO> list = new ArrayList<>();

        SysPropertyDTO serverUrl0 = SysPropertyDTO.builder()
                .grouping(getGroup())
                .k("server-url")
                .v(serverUrl).build();
        list.add(serverUrl0);

        SysPropertyDTO realm1 = SysPropertyDTO.builder().grouping(getGroup())
                .k("realm")
                .v(realm).build();
        list.add(realm1);

        SysPropertyDTO clientId0 = SysPropertyDTO.builder().grouping(getGroup())
                .k("client-id")
                .v(clientId).build();
        list.add(clientId0);

        SysPropertyDTO username1 = SysPropertyDTO.builder()
                .grouping(getGroup())
                .k("username")
                .v(username)
                .build();
        list.add(username1);

        SysPropertyDTO password1 = SysPropertyDTO.builder()
                .grouping(getGroup())
                .k("password")
                .v(password)
                .build();
        list.add(password1);

        return list;
    }
}
