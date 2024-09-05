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

package org.okstar.platform.billing.rpc.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.EnumUtils;
import org.okstar.cloud.defines.PayDefines;
import org.okstar.platform.billing.dto.GoodsDTO;
import org.okstar.platform.billing.dto.OrderDTO;
import org.okstar.platform.billing.order.domain.BillingOrder;
import org.okstar.platform.billing.order.service.BillingGoodsService;
import org.okstar.platform.billing.order.service.BillingOrderService;
import org.okstar.platform.billing.rpc.BillingOrderRpc;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.core.rpc.RpcResult;

import java.util.List;

@Transactional
@ApplicationScoped
public class BillingOrderRpcImpl implements BillingOrderRpc {

    @Inject
    BillingOrderService billingOrderService;
    @Inject
    BillingGoodsService billingGoodsService;


    @Override
    public RpcResult<OrderDTO> get(Long id) {
        var order = billingOrderService.get(id);
        OkAssert.notNull(order, "order is null");
        return RpcResult.success(toOrderDTO(order));
    }

    @Override
    public RpcResult<OrderDTO> get(String uuid) {
        var order = billingOrderService.get(uuid);
        OkAssert.notNull(order, "order is null");
        return RpcResult.success(toOrderDTO(order));
    }

    @Override
    public RpcResult<List<OrderDTO>> list(String status) {
        PayDefines.OrderStatus orderStatus = EnumUtils.getEnum(PayDefines.OrderStatus.class, status);
        List<OrderDTO> list = billingOrderService.find(orderStatus).stream().map(this::toOrderDTO).toList();
        return RpcResult.success(list);
    }

    private OrderDTO toOrderDTO(BillingOrder e) {
        var goods = billingGoodsService.findByOrderId(e.id).stream()
                .map(g -> GoodsDTO.builder().no(g.getNo()).name(g.getName()).build())
                .toList();
        //order
        return OrderDTO.builder()
                .id(e.id)
                .no(e.getNo())
                .name(e.getName())
                .uuid(e.getUuid())
                .appUuid(e.getAppUuid())
                .planUuid(e.getPlanUuid())
                .goods(goods)
                .build();
    }
}
