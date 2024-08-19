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
import org.okstar.platform.billing.dto.GoodsDTO;
import org.okstar.platform.billing.dto.OrderDTO;
import org.okstar.platform.billing.order.service.BillingGoodsService;
import org.okstar.platform.billing.order.service.BillingOrderService;
import org.okstar.platform.billing.rpc.BillingOrderRpc;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.rpc.RpcResult;

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

        //goods
        var goods = billingGoodsService.findByOrderId(order.id).stream()
                .map(g -> GoodsDTO.builder().no(g.getNo()).name(g.getName()).build())
                .toList();
        // order
        var goodsDTO = OrderDTO.builder().id(order.id).no(order.getNo()).name(order.getName())
                .goods(goods).build();

        return RpcResult.success(goodsDTO);
    }

    @Override
    public RpcResult<List<OrderDTO>> list() {
        List<OrderDTO> list = billingOrderService.findAll().stream()
                .map(e -> {
                            //goods
                            var goods = billingGoodsService.findByOrderId(e.id).stream()
                                    .map(g -> GoodsDTO.builder().no(g.getNo()).name(g.getName()).build())
                                    .toList();
                            //order
                            return OrderDTO.builder().id(e.id).no(e.getNo()).name(e.getName())
                                    .goods(goods).build();
                        }

                ).toList();
        return RpcResult.success(list);
    }
}
