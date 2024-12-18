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

package org.okstar.platform.org.connect.connector;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.connect.connector.dingtalk.SysConnectorDT;
import org.okstar.platform.org.connect.connector.feishu.SysConnectorFS;
import org.okstar.platform.org.connect.connector.wx.SysConnectorWX;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;

import java.util.HashMap;
import java.util.Map;

/**
 *  `组织模块`第三方连接器工厂
 */
@Slf4j
@ApplicationScoped
public class ConnectorFactory {

    Map<ConnectorDefines.Type, OrgConnector> pool = new HashMap<>();

    /**
     * 获取连接没有即创建
     *
     * @param conf
     * @return
     */
    public synchronized OrgConnector getConnect(OrgIntegrateConf conf) {
        OkAssert.isTrue(conf != null && conf.getType() != null, "Invalid configuration!");
        if (pool.containsKey(conf.getType())) {
            return pool.get(conf.getType());
        }
        OrgConnector connector = createConnector(conf);
        pool.put(conf.getType(), connector);
        return connector;
    }

    /**
     * 创建连接
     *
     * @param conf
     * @return
     */
    public OrgConnector createConnector(OrgIntegrateConf conf) {
        OkAssert.isTrue(conf != null && conf.getType() != null, "Invalid configuration!");

        ConnectorDefines.Type type = conf.getType();
        log.info("Create connector for: {}", type);

        var connector = switch (type) {
            case DD -> new SysConnectorDT(conf);
            case FS -> new SysConnectorFS(conf);
            case WX -> new SysConnectorWX(conf);
        };

        log.info("connector=>{}", connector);
        return connector;
    }
}
