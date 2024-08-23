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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.billing.dto.OrderDTO;
import org.okstar.platform.billing.rpc.BillingOrderRpc;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.tenant.dto.InstanceCreateDTO;
import org.okstar.platform.tenant.dto.InstanceDTO;
import org.okstar.platform.tenant.dto.TenantDTO;
import org.okstar.platform.tenant.entity.TenantEntity;
import org.okstar.platform.tenant.manager.InstanceManager;
import org.okstar.platform.tenant.service.InstanceService;
import org.okstar.platform.tenant.service.TenantService;
import org.okstar.platform.work.dto.AppDTO;
import org.okstar.platform.work.rpc.WorkAppRpc;

import java.util.List;

@Path("instance")
public class InstanceResource extends BaseResource {
    @Inject
    InstanceManager instanceManager;
    @Inject
    InstanceService instanceService;
    @Inject
    TenantService tenantService;
    @Inject
    @RestClient
    WorkAppRpc workAppRpc;
    @Inject
    @RestClient
    BillingOrderRpc billingOrderRpc;


    @PUT
    @Path("start")
    public Res<Boolean> start(Long id) {
        instanceManager.start(id);
        return Res.ok(true);
    }

    @PUT
    @Path("stop")
    public Res<Boolean> stop(Long id) {
        instanceManager.stop(id);
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

    @POST
    @Path("page")
    public Res<OkPageResult<InstanceDTO>> page(OkPageable page){
        OkPageResult<InstanceDTO> result = instanceService.findPageDTO(page);
        return Res.ok(result);
    }

    @POST
    @Path("save")
    public Res<Long> save(InstanceCreateDTO tenant) {
        Long id = instanceManager.create(tenant, self());
        return Res.ok(id);
    }

    /**
     * 租户列表
     *
     * @return Res<List < TenantDTO>>
     */
    @GET
    @Path("tenants")
    public Res<List<TenantDTO>> tenants() {
        List<TenantEntity> all = tenantService.findAll();
        List<TenantDTO> list = all.stream().map(e -> //
                        TenantDTO.builder()
                                .name(e.getName()).id(e.id)
                                .build())//
                .toList();
        return Res.ok(list);
    }

    /**
     * 订单列表
     */
    @GET
    @Path("orders")
    public Res<List<OrderDTO>> orders() {
        //列出完成的订单
        List<OrderDTO> list = RpcAssert.isTrue(billingOrderRpc.list("completed"));
        return Res.ok(list);
    }

    /**
     * 订单列表
     */
    @GET
    @Path("apps")
    public Res<List<AppDTO>> apps() {
        List<AppDTO> list = RpcAssert.isTrue(workAppRpc.list());
        return Res.ok(list);
    }
}
