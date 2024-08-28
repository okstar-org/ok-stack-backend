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

package org.okstar.platform.org.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.OkCloudFactory;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.FederalStateEntity;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.os.HostInfo;
import org.okstar.platform.common.os.HostUtils;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.org.domain.Org;

@ApplicationScoped
public class FederalServiceImpl implements FederalService {

    @Inject
    OrgService orgService;


    @Override
    public void save(Org org) {
        OkCloudApiClient client = OkCloudFactory.makeClient(OkCloudDefines.OK_CLOUD_API_STACK, OkCloudDefines.OK_CLOUD_USERNAME, OkCloudDefines.OK_CLOUD_PASSWORD);
        String orgNo = org.getNo();
        if (OkStringUtil.isEmpty(orgNo)) {
            return;
        }

        String orgName = org.getName();
        if (OkStringUtil.isEmpty(orgName)) {
            return;
        }

        try {
            FederalStateEntity stateEntity = new FederalStateEntity();
            stateEntity.setNo(org.getNo());
            stateEntity.setName(org.getName());

            //获取主机信息
            HostInfo info = HostUtils.getHostInfo();
            stateEntity.setPublicIp(info.getPublicIp());
            stateEntity.setHostName(info.getHostName());
            stateEntity.setFqdn(info.getFqdn());

            //获取提交通道
            FederalChannel channel = client.getFederalChannel();
            String cert = channel.acquireCert(stateEntity);
            Log.infof("Org[%s] cert=>%s", org.getName(), cert);

            if (cert != null) {
                orgService.setCert(org.id, cert);
            }
        } catch (Exception e) {
            Log.warnf(e.getMessage());
        }
    }
}
