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

package org.okstar.platform.org;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.cloud.entity.FederalStateEntity;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.os.HostInfo;
import org.okstar.platform.common.os.HostUtils;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.system.dto.SysConfIntegrationDTO;
import org.okstar.platform.system.rpc.SysConfIntegrationRpc;

@ApplicationScoped
public class Started {

    OkCloudApiClient client;

    @Inject
    OrgService orgService;

    @Inject
    @RestClient
    SysConfIntegrationRpc settingsRpc;


    public Started() {
        client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API_STACK,
                new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME, OkCloudDefines.OK_CLOUD_PASSWORD));
    }


    @Scheduled(every = "1m")
    public void pingTask() {
        doPing();
    }

    public void doPing() {
        Org org = orgService.loadCurrent();
        String orgNo = org.getNo();
        if (OkStringUtil.isEmpty(orgNo)) {
            return;
        }

        String orgName = org.getName();
        if (OkStringUtil.isEmpty(orgName)) {
            return;
        }


        /**
         * 获取全局配置
         */
        SysConfIntegrationDTO integrationDTO;
        try {
            integrationDTO = settingsRpc.getIntegrationConf();
            if (integrationDTO == null) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        if (OkStringUtil.isEmpty(integrationDTO.getStack().getFqdn())
                || OkStringUtil.isEmpty(integrationDTO.getIm().getHost())) {
            return;
        }

        FederalStateEntity ex = new FederalStateEntity();
        ex.setNo(org.getNo());
        ex.setName(org.getName());
        ex.setStackUrl(integrationDTO.getStack().getFqdn());
        ex.setXmppHost(integrationDTO.getIm().getHost());

        HostInfo info = null;
        try {
            //获取主机信息
            info = HostUtils.getHostInfo();
            ex.setPublicIp(info.getPublicIp());
            ex.setHostName(info.getHostName());
            ex.setPublicIp(info.getPublicIp());
            ex.setFqdn(info.getFqdn());
        } catch (Throwable e) {
            Log.warnf("getHostInfo:%s", e.getMessage());
            return;
        }

        try {
            //获取提交通道
            FederalChannel channel = client.getFederalChannel();
            String cert = channel.ping(ex);
            Log.infof("Org cert=>%s", cert);
            if (cert != null) {
                orgService.setCert(org.id, cert);
            }
        } catch (Exception e) {
            Log.warnf(e.getMessage());
        }
    }
}
