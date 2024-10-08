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

package org.okstar.platform.billing.order.schedule;

import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.cloud.entity.PayOrderEntity;
import org.okstar.platform.billing.order.domain.BillingOrder;
import org.okstar.platform.billing.order.service.BillingOrderService;
import org.okstar.platform.core.OkCloudDefines;

@Transactional
@ApplicationScoped
public class Schedule {
    private final OkCloudApiClient client;
    @Inject
    private BillingOrderService billingOrderService;

    public Schedule() {
        client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API_STACK,
                new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME,
                        OkCloudDefines.OK_CLOUD_PASSWORD));
    }

    @Scheduled(every = "1m")
    public void orderTask() {
        /**
         * 查询未同步的订单
         */
        var list = billingOrderService.notSyncList();
        list.filter(e -> e.getNo() != null).forEach(this::syncPaymentStatus);
    }

    private void syncPaymentStatus(BillingOrder bo) {
        Log.infof("Sync order[%s] payment status...", bo.getNo());
        PayOrderEntity order = client.getOrderChannel().get(bo.getNo());
        if (order == null) {
            Log.warnf("order[%s] not found", bo.getNo());
            return;
        }
        if (order.getPaymentStatus() != null) {
            Log.infof("Order[%s] payment status is [%s]", bo.getNo(), order.getPaymentStatus());
            //避免空对象的情况
            bo.setPaymentStatus(order.getPaymentStatus());
            bo.setOrderStatus(order.getOrderStatus());
            bo.setIsExpired(order.getIsExpired());
            //同步标志设置成功
            bo.setSync(true);
        }
    }

}
