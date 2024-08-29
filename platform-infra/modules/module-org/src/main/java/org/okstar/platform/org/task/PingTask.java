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

package org.okstar.platform.org.task;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.cloud.entity.FederalStatePingEntity;
import org.okstar.cloud.entity.FederalStatePongEntity;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.os.HostInfo;
import org.okstar.platform.common.os.HostUtils;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.system.rpc.SysConfIntegrationRpc;

@ApplicationScoped
public class PingTask {

    @Inject
    OrgService orgService;

    @Inject
    @RestClient
    SysConfIntegrationRpc integrationRpc;


    @Scheduled(every = "1m")
    public void pingTask() {
        try {
            Log.infof("Ping task...");
            OkCloudApiClient client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API_STACK,
                    new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME,
                            OkCloudDefines.OK_CLOUD_PASSWORD));
            doPing(client);
        } catch (Exception e) {
            Log.warnf("Task execute error: %s", e.getMessage());
        }
    }

    public void doPing(OkCloudApiClient client) {
        Org org = orgService.loadCurrent();
        String cert = org.getCert();
        if (OkStringUtil.isEmpty(cert)) {
            return;
        }

        try {
            //获取主机信息
            HostInfo info = HostUtils.getHostInfo();

            var ex = new FederalStatePingEntity();
            ex.setPublicIp(info.getPublicIp());
            ex.setHostName(info.getHostName());
            ex.setPublicIp(info.getPublicIp());
            ex.setFqdn(info.getFqdn());
            ex.setPid(info.getPid());
            ex.setTs(OkDateUtils.now());
            ex.setNo(org.getNo());
            ex.setName(org.getName());

            //获取提交通道
            FederalChannel channel = client.getFederalChannel();
            FederalStatePongEntity pongEntity = channel.ping(cert, ex);
            Log.infof("Pong=>%s", pongEntity);

            if (pongEntity.getSignals() != null && !pongEntity.getSignals().isConf()) {
                //没有上传配置
                integrationRpc.uploadConf();
            }
        } catch (Exception e) {
            Log.warnf(e.getMessage());
        }
    }
}
