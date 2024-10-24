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

package org.okstar.platform.system.conf.service;

import io.minio.MinioClient;
import io.minio.messages.Bucket;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.keycloak.admin.client.Keycloak;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.OkCloudFactory;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.FederalStateConfEntity;
import org.okstar.platform.common.exception.OkRuntimeException;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.common.web.OkWebUtil;
import org.okstar.platform.core.OkCloudDefines;
import org.okstar.platform.core.rpc.RpcAssert;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.rpc.OrgRpc;
import org.okstar.platform.system.conf.domain.*;
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysConfIntegrationServiceImpl implements SysConfIntegrationService {

    @Inject
    SysPropertyService propertyService;
    @Inject
    @RestClient
    OrgRpc orgRpc;

    @Inject
    SysKeycloakService keycloakService;

    @Override
    public SysConfIntegration find() {
        SysConfIntegration integration = new SysConfIntegration();

        //IM
        var im = new SysConfIntegrationIm();
        im.addProperties(propertyService.toDTOs(propertyService.findByGroup(im.getGroup())));
        integration.setIm(im);

        //Stack
        var stack = new SysConfIntegrationStack();
        stack.addProperties(propertyService.toDTOs(propertyService.findByGroup(stack.getGroup())));
        integration.setStack(stack);

        //Keycloak
        var keycloak = new SysConfIntegrationKeycloak();
        keycloak.addProperties(propertyService.toDTOs(propertyService.findByGroup(keycloak.getGroup())));
        integration.setKeycloak(keycloak);

        var minio = new SysConfIntegrationMinio();
        minio.addProperties(propertyService.toDTOs(propertyService.findByGroup(minio.getGroup())));
        integration.setMinio(minio);

        return integration;
    }

    @Override
    public void save(SysConfItem item) {
        List<SysPropertyDTO> list = item.getProperties();
        list.forEach(property -> {
            Optional<SysProperty> optional = propertyService.findByKey(property.getGrouping(), property.getK());
            if (optional.isEmpty()) {
                SysProperty create = new SysProperty();
                create.setGrouping(property.getGrouping());
                create.setK(property.getK());
                create.setV(property.getV());
                propertyService.create(create, 1L);
            } else {
                SysProperty update = optional.get();
                update.setGrouping(property.getGrouping());
                update.setK(property.getK());
                update.setV(property.getV());
                propertyService.update(update, 1L);
            }
        });
    }

    @Override
    public void save(SysConfIntegration integration) {
        Log.infof("Save integration [%s]", integration);

        //im
        SysConfIntegrationIm im = integration.getIm();
//        saveIm(im);
        save(im);

        //stack
        SysConfIntegrationStack stack = integration.getStack();
//        saveStack(stack);
        save(stack);

        //keycloak
        SysConfIntegrationKeycloak keycloak = integration.getKeycloak();
//        saveKeycloak(keycloak);
        save(keycloak);

        uploadConf(integration);
    }

    /**
     * 上传配置
     */
    @Override
    public void uploadConf(SysConfIntegration integration) {
        Log.infof("Upload integration [%s]", integration);

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
        x.setImPort(im.getPort());

        SysConfIntegrationStack stack = integration.getStack();
        x.setStackUrl(stack.getFqdn());

        try {
            Log.infof("Put config to cloud...");
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

    @Override
    public boolean testKeycloak(SysConfIntegrationKeycloak conf) {
        try (Keycloak keycloak = keycloakService.openKeycloak(conf)) {
            return keycloak != null && keycloak.serverInfo().getInfo() != null;
        }
    }

    @Override
    public boolean testIm(SysConfIntegrationIm conf) {
        try {
            URL url = new URL("http", conf.getHost(), conf.getAdminPort(), "");
            String content = OkWebUtil.get(url.toURI());
            Log.infof(content);
            return OkStringUtil.isNotEmpty(content);
        } catch (MalformedURLException | URISyntaxException e) {
            Log.warnf("url:%s is invalid:%s", conf.getHost(), e.getMessage());
            return false;
        }
    }

    @Override
    public boolean testStack(SysConfIntegrationStack conf) {
        String fqdn = conf.getFqdn();
        String content = OkWebUtil.get(fqdn);
        Log.infof(content);
        return OkStringUtil.isNotEmpty(content);
    }

    @Override
    public boolean testMinio(SysConfIntegrationMinio conf) {
        try {
            var client = MinioClient.builder().endpoint(conf.getEndpoint())
                    .credentials(conf.getAccessKey(), conf.getSecretKey())
                    .build();
            List<Bucket> buckets = client.listBuckets();
            Log.infof("buckets: %s", buckets.size());

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
