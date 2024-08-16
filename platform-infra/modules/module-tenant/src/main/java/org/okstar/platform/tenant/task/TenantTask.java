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

package org.okstar.platform.tenant.task;

import com.github.dockerjava.api.model.Container;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.tenant.defines.TenantDefined;
import org.okstar.platform.tenant.dto.TenantMetaDTO;
import org.okstar.platform.tenant.entity.MetaEntity;
import org.okstar.platform.tenant.entity.TenantEntity;
import org.okstar.platform.tenant.os.DockerService;
import org.okstar.platform.tenant.service.MetaService;
import org.okstar.platform.tenant.service.TenantService;
import org.okstar.platform.tenant.utils.TenantUtil;

import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class TenantTask {

    @Inject
    TenantService tenantService;
    @Inject
    DockerService dockerService;
    @Inject
    MetaService metaService;
    @Inject
    TenantUtil tenantUtil;
    @Inject
    ExecutorService executorService;


    @Scheduled(every = "1m")
    public void statusSyncTask() {
        executorService.execute(()->{
            for (TenantEntity tenant : tenantService.findAll()) {
                statusSync(tenant.id);
            }
        });
    }

    /**
     * 状态同步
     *
     * @param tenantId 租户id
     */
    @Transactional
    public void statusSync(Long tenantId) {
         Log.infof("Status sync tenant[%s]", tenantId);

        TenantEntity tenant = tenantService.get(tenantId);
        if (tenant == null) {
            Log.warnf("Tenant tenant[%s] is null!", tenantId);
            return;
        }

        MetaEntity meta = metaService.loadByTenant(tenant.id);
        if (meta == null) {
            Log.warnf("Tenant[%s] meta is null!");
            tenant.setStatus(TenantDefined.TenantStatus.Error);
            return;
        }

        TenantMetaDTO metaDTO = tenantUtil.toMetaDTO(meta.getJsonValue());
        if (metaDTO == null) {
            Log.warnf("Tenant[%s] meta.jsonValue is null!", tenant.id);
            tenant.setStatus(TenantDefined.TenantStatus.Error);
            return;
        }

        TenantMetaDTO.DB db = metaDTO.getDb();
        if (db == null) {
            Log.warnf("Tenant[%s] meta.db is null!", tenant.id);
            tenant.setStatus(TenantDefined.TenantStatus.Error);
            return;
        }

        String containerId = db.getContainerId();
        if(containerId == null) {
            Log.warnf("Tenant[%s] meta.db.containerId is null!", tenant.id);
            tenant.setStatus(TenantDefined.TenantStatus.Error);
            return;
        }

        Container container = dockerService.getContainer(containerId);
        if (container != null && OkStringUtil.equals(container.getState(), "running")  ) {
            //update to Stopped
            tenant.setStatus(TenantDefined.TenantStatus.Started);
        } else {
            //update to Started
            tenant.setStatus(TenantDefined.TenantStatus.Stopped);
        }
    }
}