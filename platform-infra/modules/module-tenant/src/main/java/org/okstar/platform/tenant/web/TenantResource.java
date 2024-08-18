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
import jakarta.ws.rs.*;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.tenant.dto.TenantCreateDTO;
import org.okstar.platform.tenant.dto.TenantDetailDTO;
import org.okstar.platform.tenant.dto.TenantUpdateDTO;
import org.okstar.platform.tenant.entity.TenantEntity;
import org.okstar.platform.tenant.manager.TenantManager;
import org.okstar.platform.tenant.service.TenantService;

@Path("tenant")
public class TenantResource extends OkCommonResource {

    @Inject
    TenantService tenantService;
    @Inject
    TenantManager tenantManager;

    @GET
    @Path("detail/{id}")
    public Res<TenantDetailDTO> detail(@PathParam("id") Long id) {
        TenantDetailDTO detailDTO  = tenantManager.loadDetail(id);
        return Res.ok(detailDTO);
    }

    @PUT
    @Path("start")
    public Res<Boolean> start(Long id) {
        tenantManager.start(id);
        return Res.ok(true);
    }

    @PUT
    @Path("stop")
    public Res<Boolean> stop(Long id) {
        tenantManager.stop(id);
        return Res.ok(true);
    }

    @PUT
    @Path("disable")
    public Res<Boolean> disable(Long id) {
        tenantService.disable(id);
        return Res.ok(true);
    }

    @PUT
    @Path("enable")
    public Res<Boolean> enable(Long id) {
        tenantService.enable(id);
        return Res.ok(true);
    }

    @PUT
    @Path("update")
    public Res<Long> update(TenantUpdateDTO tenant) {
        Long id = tenantManager.updateTenant(tenant);
        return Res.ok(id);
    }


    @POST
    @Path("save")
    public Res<Long> save(TenantCreateDTO tenant) {
        Long id = tenantManager.create(tenant);
        return Res.ok(id);
    }

    @POST
    @Path("page")
    public Res<OkPageResult<TenantEntity>> list(OkPageable page){
        OkPageResult<TenantEntity> result = tenantService.findPage(page);
        return Res.ok(result);
    }




}