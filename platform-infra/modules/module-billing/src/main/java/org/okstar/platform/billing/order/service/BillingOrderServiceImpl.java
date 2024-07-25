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

package org.okstar.platform.billing.order.service;

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.channel.OrderChannel;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.cloud.entity.OrderResultEntity;
import org.okstar.platform.billing.order.domain.BillingOrder;
import org.okstar.platform.billing.order.mapper.BillingOrderMapper;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;

import java.util.List;
import java.util.stream.Stream;

@Transactional
@ApplicationScoped
public class BillingOrderServiceImpl implements BillingOrderService {
    @Inject
    BillingOrderMapper orderMapper;

    OkCloudApiClient client;


    public BillingOrderServiceImpl() {
        client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API_STACK,
                new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME, OkCloudDefines.OK_CLOUD_PASSWORD));
    }

    @Override
    public Stream<BillingOrder> notSyncList() {
        /**
         * 查询未同步的订单
         */
        var list = orderMapper.find("sync is null or sync = false").list();
        return list.stream().filter(e -> e.getNo() != null);
    }

    @Override
    public void save(BillingOrder billingOrder) {
        orderMapper.persist(billingOrder);
    }

    @Override
    public List<BillingOrder> findAll() {
        return orderMapper.findAll().list();
    }

    @Override
    public OkPageResult<BillingOrder> findPage(OkPageable page) {
        var paged = orderMapper
                .findAll(Sort.descending("id"))
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());
    }

    @Override
    public BillingOrder get(Long id) {
        return orderMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        orderMapper.deleteById(id);
    }

    @Override
    public void delete(BillingOrder billingOrder) {
        orderMapper.delete(billingOrder);
    }

    /**
     * 保存订单
     */
    @Override
    public void saveResult(OrderResultEntity result, Long createBy) {
        Log.infof("保存订单：%s", result);
        BillingOrder order = new BillingOrder();
        OkBeanUtils.copyPropertiesTo(result.getOrder(), order);

        /**
         * 该3项字段通过同步更新
         */
        order.setIsExpired(null);
        order.setOrderStatus(null);
        order.setPaymentStatus(null);

        create(order, createBy);
    }

    @Override
    public OrderResultEntity createOrder(Long planId, Long createBy) {
        OrderChannel orderChannel = client.getOrderChannel();
        OrderResultEntity result = orderChannel.create(planId);
        result.setUrl(OkCloudDefines.OK_CLOUD_API+result.getUrl());
        saveResult(result, createBy);
        return result;
    }

    @Override
    public boolean closeOrder(String no, Long createBy) {
        OrderChannel orderChannel = client.getOrderChannel();
        return orderChannel.close(no);
    }
}
