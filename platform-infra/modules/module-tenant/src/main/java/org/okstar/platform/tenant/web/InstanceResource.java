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

package org.okstar.platform.tenant.web;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.tenant.dto.InstanceCreateDTO;
import org.okstar.platform.tenant.dto.TenantDTO;
import org.okstar.platform.tenant.entity.TenantEntity;
import org.okstar.platform.tenant.manager.InstanceManager;
import org.okstar.platform.tenant.service.TenantService;

import java.util.List;

@Path("instance")
public class InstanceResource extends OkCommonResource {
    @Inject
    InstanceManager instanceManager;
    @Inject
    TenantService tenantService;

    @POST
    @Path("save")
    public Res<Long> save(InstanceCreateDTO tenant) {
        Long id = instanceManager.create(tenant);
        return Res.ok(id);
    }

    @GET
    @Path("tenants")
    public Res<List<TenantDTO>> tenants(InstanceCreateDTO tenant) {
        List<TenantEntity> all = tenantService.findAll();
        List<TenantDTO> list = all.stream().map(e -> //
                        TenantDTO.builder()
                                .name(e.getName()).id(e.id)
                                .build())//
                .toList();
        return Res.ok(list);
    }
}
