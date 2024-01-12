/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
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
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.okstar.cloud.entity.OrderResultEntity;
import org.okstar.platform.billing.order.service.BillingOrderService;
import org.okstar.platform.billing.resource.BillingBaseResource;
import org.okstar.platform.common.core.web.bean.Res;

@Authenticated
@Path("order")
public class BillingOrderResource extends BillingBaseResource {


    @Inject
    BillingOrderService orderService;

    public BillingOrderResource() {

    }

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
        orderService.closeOrder(no, loadUserId());
        return Res.ok(true);
    }


}
