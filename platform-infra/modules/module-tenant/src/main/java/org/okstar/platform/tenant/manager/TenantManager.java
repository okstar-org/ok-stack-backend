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

import org.okstar.platform.tenant.dto.TenantCreateDTO;
import org.okstar.platform.tenant.dto.TenantDetailDTO;
import org.okstar.platform.tenant.dto.TenantUpdateDTO;

public interface TenantManager {
    /**
     * 创建租户
     * @param tenant
     * @return id
     */
    Long createTenant(TenantCreateDTO tenant);


    void createResource(Long tenantId);

    void startContainer(String containerId);

    Long updateTenant(TenantUpdateDTO tenant);

    void start(Long tenantId);

    void stop(Long tenantId);

    /**
     * 加载详情
     * @param tenantId 租户ID
     * @return TenantDetailDTO
     */
    TenantDetailDTO loadDetail(Long tenantId);
}
