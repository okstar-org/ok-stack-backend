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

package org.okstar.platform.billing.order.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.okstar.cloud.entity.OrderResultEntity;
import org.okstar.platform.billing.order.domain.BillingOrder;
import org.okstar.platform.billing.order.service.BillingOrderService;
import org.okstar.platform.billing.resource.BillingBaseResource;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;

@Authenticated
@Path("order")
public class BillingOrderResource extends BillingBaseResource {


    @Inject
    BillingOrderService orderService;

    /**
     * 创建订单
     * @return
     */
    @POST
    @Path("create")
    public Res<OrderResultEntity> create(Long planId) {
        var order = orderService.createOrder(planId, loadUserId());
        return Res.ok(order);
    }
    /**
     * 关闭订单
     * @return
     */
    @POST
    @Path("close")
    public Res<Boolean> close(String no) {
        boolean closeOrder = orderService.closeOrder(no, loadUserId());
        return Res.ok(closeOrder);
    }


    /**
     * 列表查询
     * @param pageable
     * @return
     */
    @POST
    @Path("page")
    public Res<OkPageResult<BillingOrder>> page(OkPageable pageable) {
        var result = orderService.findPage(pageable);
        return Res.ok(result);
    }

    /**
     * 订单明细
     * @param id
     * @return
     */
    @GET
    @Path("/detail/{id}")
    public Res<BillingOrder> detail(@PathParam("id") Long id) {
        var app = orderService.get(id);
        return Res.ok(app);
    }
}
