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
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.system.vo.SysAccount0;
import org.okstar.platform.tenant.defines.TenantDefined;
import org.okstar.platform.tenant.dto.InstanceCreateDTO;
import org.okstar.platform.tenant.entity.InstanceEntity;
import org.okstar.platform.tenant.service.InstanceService;
import org.okstar.platform.work.rpc.WorkAppRpc;

import java.util.concurrent.ExecutorService;

@Transactional
@ApplicationScoped
public class InstanceManagerImpl implements InstanceManager {

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

        Log.infof("Create tenant: %s", createDTO);

        var orderDTO = RpcAssert.isTrue(billingOrderRpc.get(createDTO.getOrderId()));


        InstanceEntity instanceEntity = new InstanceEntity();
        instanceEntity.setTenantId(createDTO.getTenantId());
        instanceEntity.setAppId(createDTO.getAppId());
        instanceEntity.setOrderId(createDTO.getOrderId());
        instanceEntity.setUuid(OkIdUtils.makeUuid());
        instanceEntity.setDisabled(false);
        instanceEntity.setName(orderDTO.getName());
        instanceService.create(instanceEntity, account.getId());


        //初始化实例环境
        ExecutorService executorService = Arc.container().getExecutorService();
        executorService.execute(() -> {
            createInstance(instanceEntity.id);
        });

        return instanceEntity.id;
    }

    @Override
    public void start(Long id) {
        InstanceEntity entity = instanceService.get(id);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }
//
//        TenantMetaDoc metaDTO = metaDocMapper.getMetaDoc(entity.id);
//        OkAssert.notNull(metaDTO, "没有资源元数据！");
//        for (var db : metaDTO.getDbs()) {
//            startContainer(db.getContainerId());
//        }

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
//        TenantMetaDoc metaDTO = metaDocMapper.getMetaDoc(entity.id);
//        OkAssert.notNull(metaDTO, "没有资源元数据！");
//
//        for (var db : metaDTO.getDbs()) {
//            stopContainer(db.getContainerId());
//        }
        entity.setStatus(TenantDefined.TenantStatus.Stopped);
    }

    /**
     * 创建实例服务
     *
     * @param instanceId 实例ID
     */
    private void createInstance(Long instanceId) {
        InstanceEntity instance = instanceService.get(instanceId);
        OkAssert.notNull(instance, "实例信息不存在，无法创建服务！");


    }
}
