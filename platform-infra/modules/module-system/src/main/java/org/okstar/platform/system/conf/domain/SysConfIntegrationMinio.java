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
import org.okstar.platform.system.conf.SysConfDefines;
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 系统管理-集成设置-minio存储配置
 */
@Data
public class SysConfIntegrationMinio implements SysConfItem {

    private String endpoint;

    private String accessKey;

    private String secretKey;

    private String externalUrl;

    public String getValidExternalUrl() {
        return Optional.ofNullable(getExternalUrl()).orElse(getEndpoint());
    }

    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_INTEGRATION_PREFIX + ".minio";
    }

    @Override
    public void addProperty(SysPropertyDTO property) {
        String k = property.getK();
        switch (k) {
            case "endpoint":
                this.endpoint = property.getV();
                break;
            case "accessKey":
                this.accessKey = property.getV();
                break;
            case "secretKey":
                this.secretKey = property.getV();
                break;
            case "externalUrl":
                this.externalUrl = property.getV();
                break;
        }
    }

    @Override
    public List<SysPropertyDTO> getProperties() {
        List<SysPropertyDTO> list = new ArrayList<>();

        SysPropertyDTO s = SysPropertyDTO.builder()
                .grouping(getGroup())
                .k("endpoint")
                .v(endpoint)
                .build();
        list.add(s);

        SysPropertyDTO credentials1 = SysPropertyDTO.builder()
                .grouping(getGroup())
                .k("accessKey")
                .v(accessKey).build();
        list.add(credentials1);

        SysPropertyDTO secretKey1 = SysPropertyDTO.builder()
                .grouping(getGroup())
                .k("secretKey")
                .v(secretKey).build();
        list.add(secretKey1);

        SysPropertyDTO externalUrl1 = SysPropertyDTO.builder()
                .grouping(getGroup())
                .k("externalUrl")
                .v(externalUrl)
                .build();
        list.add(externalUrl1);

        return list;
    }
}
