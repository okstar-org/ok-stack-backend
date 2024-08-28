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

package org.okstar.platform.system.settings.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.OkCloudFactory;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.FederalStateConfEntity;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.rpc.OrgRpc;
import org.okstar.platform.system.settings.domain.*;
import org.okstar.platform.system.settings.mapper.SysSetKvMapper;

import java.io.IOException;
import java.util.List;

@Transactional
@ApplicationScoped
public class SysConfIntegrationServiceImpl implements SysConfIntegrationService {

    @Inject
    SysSetKvMapper kvMapper;
    @Inject
    @RestClient
    OrgRpc orgRpc;

    @Override
    public SysConfIntegration find() {
        SysConfIntegration integration = new SysConfIntegration();

        //IM
        SysConfIntegrationIm im = new SysConfIntegrationIm();
        im.addProperties(kvMapper.findByGroup(im.getGroup()));
        integration.setIm(im);

        //Stack
        SysConfIntegrationStack stack = new SysConfIntegrationStack();
        stack.addProperties(kvMapper.findByGroup(stack.getGroup()));
        integration.setStack(stack);

        //Keycloak
        SysConfIntegrationKeycloak keycloak = new SysConfIntegrationKeycloak();
        keycloak.addProperties(kvMapper.findByGroup(keycloak.getGroup()));
        integration.setKeycloak(keycloak);
        return integration;
    }

    @Override
    public void save(SysConfIntegration integration) {
        Log.infof("Save integration [%s]", integration);

        //im
        SysConfIntegrationIm im = integration.getIm();
        kvMapper.findByGroup(im.getGroup()).forEach(p -> {
            if ("host".equals(p.getK())) {
                p.setV(im.getHost());
            } else if ("admin-port".equals(p.getK())) {
                p.setV(String.valueOf(im.getAdminPort()));
            } else if ("api-secret".equals(p.getK())) {
                p.setV(im.getApiSecret());
            }
        });

        //stack
        SysConfIntegrationStack stack = integration.getStack();
        kvMapper.findByGroup(stack.getGroup()).forEach(sp -> {
            if ("fqdn".equals(sp.getK())) {
                sp.setV(stack.getFqdn());
            }
        });

        //keycloak
        SysConfIntegrationKeycloak keycloak = integration.getKeycloak();
        List<SysProperty> kcList = kvMapper.findByGroup(keycloak.getGroup());
        kcList.forEach(p -> {
            if ("server-url".equals(p.getK())) {
                p.setV(keycloak.getServerUrl());
            }
        });


        uploadConf(integration);


    }

    /**
     * 上传配置
     */
    @Override
    public void uploadConf(SysConfIntegration integration) {
        /**
         * 保存到Cloud
         */
        RpcResult<Org0> current = orgRpc.current();
        Org0 org0 = RpcAssert.isTrue(current);

        if (org0 == null || OkStringUtil.isEmpty(org0.getCert())) {
            return;
        }

        SysConfIntegrationIm im = integration.getIm();
        var x = new FederalStateConfEntity();
        x.setImHost(im.getHost());
        x.setImPort(im.getAdminPort());

        SysConfIntegrationStack stack = integration.getStack();
        x.setStackUrl(stack.getFqdn());

        try {
            OkCloudApiClient client = OkCloudFactory.makeClient(OkCloudDefines.OK_CLOUD_API_STACK,
                    OkCloudDefines.OK_CLOUD_USERNAME,
                    OkCloudDefines.OK_CLOUD_PASSWORD);

            FederalChannel channel = client.getFederalChannel();
            String putConfig = channel.putConfig(org0.getCert(), x);
            Log.infof("Put config=>%s", putConfig);

        } catch (IOException e) {
            throw new OkRuntimeException("连接到社区网络异常，请稍后再试！");
        }
    }
}
