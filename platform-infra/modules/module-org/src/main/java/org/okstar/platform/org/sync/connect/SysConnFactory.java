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

import com.google.common.collect.Maps;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.service.SysConAppService;

import java.util.List;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class SysConnFactory {

    @Inject
    private SysConAppService sysConAppService;

    private final Map<SysConEnums.SysConType, SysConnector> map = Maps.newLinkedHashMap();


    public void initConnectorPool() {
        log.info("初始化连接器...");
        List<OrgIntegrateConf> sysConApps = sysConAppService.findAll();
        sysConApps.forEach(conf -> {
            log.info("为连接器:{}查找配置=>{}", conf.getType(), sysConApps);
        });
    }


    public SysConnector getConnector(OrgIntegrateConf app) {
        log.info("getConnector:{}", app);

        OkAssert.notNull(app, "app can not be null");

        SysConEnums.SysConType type = app.getType();
        log.info("SysConType:{}", type);
        OkAssert.notNull(type, "type is null");

        SysConnector connector;
        synchronized (map) {
            connector = map.get(type);
            log.info("type:{} connector=>{}", type, connector);
            if (connector == null) {
                initConnectorPool();
                connector = map.get(type);
            }
        }

//        OkAssert.notNull(String.format("无法获取:%s的连接器:%s，可能不支持！", type, connector));

        log.info("getConnector=>{}", connector);

        return connector;
    }
}
