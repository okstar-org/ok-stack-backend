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

package org.okstar.platform.org.sync.connect.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.system.rpc.SysPropertyDTO;
import org.okstar.platform.system.rpc.SysPropertyRpc;

import java.util.List;

@ApplicationScoped
public class ConnectorConfigServiceImpl implements ConnectorConfigService {

    public static final String ORG_CONNECTOR = "org.connector";
    @Inject
    @RestClient
    SysPropertyRpc sysPropertyRpc;

    @Override
    public void save(OrgIntegrateConf conf) {
        //group
        String grouping = toGroup(conf.getType());

        //appId
        SysPropertyDTO appId = new SysPropertyDTO();
        appId.setGrouping(grouping);
        appId.setK("appId");
        appId.setV(conf.getAppId());
        sysPropertyRpc.save(appId);

        //secret
        SysPropertyDTO appSecret = new SysPropertyDTO();
        appSecret.setGrouping(grouping);
        appSecret.setK("secret");
        appSecret.setV(conf.getCertSecret());
        sysPropertyRpc.save(appSecret);

        //key
        if (conf.getCertKey() != null) {
            SysPropertyDTO appKey = new SysPropertyDTO();
            appKey.setGrouping(grouping);
            appKey.setK("key");
            appKey.setV(conf.getCertKey());
            sysPropertyRpc.save(appKey);
        }

        if (conf.getBaseUrl() != null) {
            SysPropertyDTO dto = new SysPropertyDTO();
            dto.setGrouping(grouping);
            dto.setK("baseUrl");
            dto.setV(conf.getBaseUrl());
            sysPropertyRpc.save(dto);
        }

        if (conf.getRootDeptId() != null) {
            SysPropertyDTO dto = new SysPropertyDTO();
            dto.setGrouping(grouping);
            dto.setK("rootDeptId");
            dto.setV(conf.getRootDeptId());
            sysPropertyRpc.save(dto);
        }
    }

    private static String toGroup(ConnectorDefines.Type type) {
        return ("%s.%s").formatted(ORG_CONNECTOR, type);
    }

    @Override
    public List<OrgIntegrateConf> findAll() {
        return List.of();
    }

    @Override
    public OrgIntegrateConf findOne(ConnectorDefines.Type type) {
        OrgIntegrateConf conf = new OrgIntegrateConf();
        conf.setType(type);

        String group = toGroup(type);
        for (SysPropertyDTO dto : sysPropertyRpc.getByGroup(group)) {
            switch (dto.getK()) {
                case "appId" -> conf.setAppId(dto.getV());
                case "secret" -> conf.setCertSecret(dto.getV());
                case "key" -> conf.setCertKey(dto.getV());
                case "baseUrl" -> conf.setBaseUrl(dto.getV());
                case "rootDeptId" -> conf.setRootDeptId(dto.getV());
            }
        }
        return conf;
    }
}
