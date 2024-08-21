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

package org.okstar.platform.tenant.manager;

import io.quarkus.arc.Arc;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.billing.rpc.BillingOrderRpc;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.system.vo.SysAccount0;
import org.okstar.platform.tenant.defines.TenantDefined;
import org.okstar.platform.tenant.dto.InstanceCreateDTO;
import org.okstar.platform.tenant.entity.InstanceEntity;
import org.okstar.platform.tenant.os.DockerService;
import org.okstar.platform.tenant.service.InstanceService;
import org.okstar.platform.work.dto.AppMetaDTO;
import org.okstar.platform.work.dto.RunModality;
import org.okstar.platform.work.rpc.WorkAppRpc;

import java.util.concurrent.ExecutorService;

@Transactional
@ApplicationScoped
public class InstanceManagerImpl implements InstanceManager {
    @Inject
    DockerService dockerService;
    @Inject
    InstanceService instanceService;
    @Inject
    @RestClient
    BillingOrderRpc billingOrderRpc;
    @Inject
    @RestClient
    WorkAppRpc workAppRpc;


    @Override
    public Long create(InstanceCreateDTO createDTO, SysAccount0 account) {

        Log.infof("Create instance: %s", createDTO);

        var orderDTO = RpcAssert.isTrue(billingOrderRpc.get(createDTO.getOrderId()));
        Log.infof("order: %s", orderDTO);

        AppMetaDTO appMeta = RpcAssert.isTrue(workAppRpc.meta(createDTO.getAppId()));
        Log.infof("meta: %s", appMeta);

        InstanceEntity instanceEntity = new InstanceEntity();
        instanceEntity.setTenantId(createDTO.getTenantId());
        instanceEntity.setAppId(createDTO.getAppId());
        instanceEntity.setOrderId(createDTO.getOrderId());
        instanceEntity.setDisabled(false);
        instanceEntity.setName(orderDTO.getName());
        instanceEntity.setStatus(TenantDefined.TenantStatus.Created);
        instanceService.create(instanceEntity, account.getId());


        //初始化实例环境
        ExecutorService executorService = Arc.container().getExecutorService();
        executorService.execute(() -> {
            createInstance(instanceEntity.id, appMeta);
        });

        return instanceEntity.id;
    }

    @Override
    public void start(Long id) {
        InstanceEntity entity = instanceService.get(id);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }

        AppMetaDTO appMeta = RpcAssert.isTrue(workAppRpc.meta(entity.getAppId()));
        Log.infof("meta: %s", appMeta);
        if (appMeta.getRunModality() == RunModality.DockerCompose) {
            boolean up = dockerService.up(appMeta.getRunOn());
            Log.infof("Up the instance=>%s", up);
        }
        entity.setStatus(TenantDefined.TenantStatus.Started);
    }

    @Override
    public void stop(Long id) {
        InstanceEntity entity = instanceService.get(id);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }

        if (entity.getStatus() == TenantDefined.TenantStatus.Stopped) {
            Log.warnf("Instance is already stopped!");
            return;
        }
//
        AppMetaDTO appMeta = RpcAssert.isTrue(workAppRpc.meta(entity.getAppId()));
        Log.infof("meta: %s", appMeta);
        if (appMeta.getRunModality() == RunModality.DockerCompose) {
            boolean up = dockerService.down(appMeta.getRunOn());
            Log.infof("Down the instance=>%s", up);
        }

        entity.setStatus(TenantDefined.TenantStatus.Stopped);
    }

    /**
     * 创建实例服务
     *
     * @param instanceId 实例ID
     * @param appMeta
     */
    private void createInstance(Long instanceId, AppMetaDTO appMeta) {
        InstanceEntity instance = instanceService.get(instanceId);
        OkAssert.notNull(instance, "实例信息不存在，无法创建服务！");

        RunModality runModality = appMeta.getRunModality();
        switch (runModality) {
            case DockerCompose -> {
                dockerService.up(appMeta.getRunOn());
            }
            case Url -> {

            }
        }
    }
}
