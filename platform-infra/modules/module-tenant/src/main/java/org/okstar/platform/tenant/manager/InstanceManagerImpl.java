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
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.tenant.dto.InstanceCreateDTO;
import org.okstar.platform.tenant.entity.InstanceEntity;
import org.okstar.platform.tenant.service.InstanceService;

import java.util.concurrent.ExecutorService;

@Transactional
@ApplicationScoped
public class InstanceManagerImpl implements InstanceManager {

    @Inject
    InstanceService instanceService;

    @Override
    public Long create(InstanceCreateDTO createDTO) {

        Log.infof("Create tenant: %s", createDTO);

        InstanceEntity instanceEntity = new InstanceEntity();
        instanceEntity.setTenantId(createDTO.getTenantId());
        instanceEntity.setAppId(createDTO.getAppId());
        instanceEntity.setOrderId(createDTO.getOrderId());
        instanceService.save(instanceEntity);

        //初始化实例环境
        ExecutorService executorService = Arc.container().getExecutorService();
        executorService.execute(() -> {
            createInstance(instanceEntity.id);
        });

        return instanceEntity.id;
    }

    /**
     * 创建实例服务
     * @param instanceId 实例ID
     */
    private void createInstance(Long instanceId) {
        InstanceEntity instance = instanceService.get(instanceId);
        OkAssert.notNull(instance, "实例信息不存在，无法创建服务！");



    }
}
