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

package org.okstar.platform.org.sync.connect;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.sync.connect.connector.dingtalk.SysConnectorDT;
import org.okstar.platform.org.sync.connect.connector.feishu.SysConnectorFS;
import org.okstar.platform.org.sync.connect.connector.wx.SysConnectorWX;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.service.ConnectorConfigService;

import java.util.List;

@Slf4j
@ApplicationScoped
public class ConnectorFactory {

    @Inject
    private ConnectorConfigService connectorConfigService;


    public void initConnectorPool() {
        log.info("初始化连接器...");
        List<OrgIntegrateConf> sysConApps = connectorConfigService.findAll();
        sysConApps.forEach(conf -> {
            log.info("为连接器:{}查找配置=>{}", conf.getType(), sysConApps);
        });
    }


    public SysConnector getConnector(OrgIntegrateConf conf) {
        log.info("getConnector:{}", conf);
        OkAssert.notNull(conf, "Invalid configuration!");

        ConnectorDefines.Type appType = conf.getType();
        var connector = switch (appType) {
            case DD -> new SysConnectorDT(conf);
            case FS -> new SysConnectorFS(conf);
            case WX -> new SysConnectorWX(conf);
        };

        log.info("getConnector=>{}", connector);
        return connector;
    }
}
