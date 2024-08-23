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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.SneakyThrows;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.billing.rpc.BillingOrderRpc;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.vo.SysAccount0;
import org.okstar.platform.tenant.defines.TenantDefined;
import org.okstar.platform.tenant.dto.InstanceCreateDTO;
import org.okstar.platform.tenant.entity.InstanceEntity;
import org.okstar.platform.tenant.os.DockerService;
import org.okstar.platform.tenant.service.InstanceService;
import org.okstar.platform.work.dto.AppMetaDTO;
import org.okstar.platform.work.dto.RunModality;
import org.okstar.platform.work.rpc.WorkAppRpc;

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
    @Inject
    ObjectMapper objectMapper;

    @Override
    public Long create(InstanceCreateDTO createDTO, SysAccount0 account) {

        Log.infof("Create instance: %s", createDTO);

        OkAssert.hasText(createDTO.getOrderUuid(), "参数有误！");
        var orderDTO = RpcAssert.isTrue(billingOrderRpc.get(createDTO.getOrderUuid()));
        Log.infof("order: %s", orderDTO);

        OkAssert.hasText(orderDTO.getAppUuid(), "参数有误！");
        AppMetaDTO appMeta = RpcAssert.isTrue(workAppRpc.meta(orderDTO.getAppUuid()));
        Log.infof("meta: %s", appMeta);

        InstanceEntity instanceEntity = new InstanceEntity();
        instanceEntity.setTenantId(createDTO.getTenantId());
        instanceEntity.setOrderUuid(orderDTO.getUuid());
        instanceEntity.setAppUuid(appMeta.getAppUuid());
        instanceEntity.setName(orderDTO.getName());
        instanceEntity.setStatus(TenantDefined.TenantStatus.Created);
        instanceEntity.setDisabled(false);
        instanceService.create(instanceEntity, account.getId());
        return instanceEntity.id;
    }

    @Override
    @SneakyThrows(JsonProcessingException.class)
    public void start(Long id) {
        InstanceEntity entity = instanceService.get(id);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }

        AppMetaDTO appMeta = RpcAssert.isTrue(workAppRpc.meta(entity.getAppUuid()));
        Log.infof("meta: %s", appMeta);

        OkAssert.isTrue(appMeta.getRunModality() == RunModality.DockerCompose, "目前仅支持DockerCompose应用！");
        OkAssert.isTrue(OkStringUtil.isNotEmpty(appMeta.getRunOn()), "运行配置不能为空！");

        var up = dockerService.up(appMeta.getRunOn(), entity.getUuid());
        Log.infof("Up the instance=>%s", up);

        entity.setRunning(objectMapper.writeValueAsString(up));
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

        AppMetaDTO appMeta = RpcAssert.isTrue(workAppRpc.meta(entity.getAppUuid()));
        Log.infof("meta: %s", appMeta);
        if (appMeta.getRunModality() == RunModality.DockerCompose) {
            boolean up = dockerService.down(appMeta.getRunOn(), entity.getUuid());
            Log.infof("Down the instance=>%s", up);
            entity.setRunning(null);
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
    }
}
