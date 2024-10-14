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

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.billing.order.domain.BillingGoods;
import org.okstar.platform.billing.order.domain.BillingGoods_;
import org.okstar.platform.billing.order.mapper.BillingGoodsMapper;
import org.okstar.platform.common.web.page.OkPageResult;
import org.okstar.platform.common.web.page.OkPageable;

import java.util.List;


@Transactional
@ApplicationScoped
public class BillingGoodsServiceImpl implements BillingGoodsService {


    @Inject
    BillingGoodsMapper billingGoodsMapper;


    @Override
    public void save(BillingGoods billingGoods) {
        billingGoodsMapper.persist(billingGoods);
    }

    @Override
    public List<BillingGoods> findAll() {
        return billingGoodsMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<BillingGoods> findPage(OkPageable page) {
        var paged = billingGoodsMapper
                .findAll(Sort.descending("id"))
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());

    }

    @Override
    public BillingGoods get(Long id) {
        return billingGoodsMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        billingGoodsMapper.deleteById(id);
    }

    @Override
    public void delete(BillingGoods billingGoods) {
        billingGoodsMapper.delete(billingGoods);
    }

    @Override
    public BillingGoods get(String uuid) {
        return billingGoodsMapper.find("uuid", uuid).firstResult();
    }

    @Override
    public List<BillingGoods> findByOrderId(Long orderId) {
        return billingGoodsMapper.find(BillingGoods_.ORDER_ID, orderId).stream().toList();
    }
}
