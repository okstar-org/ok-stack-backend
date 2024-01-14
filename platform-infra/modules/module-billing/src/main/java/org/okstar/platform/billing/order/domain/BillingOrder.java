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

package org.okstar.platform.billing.order.domain;

import jakarta.persistence.Entity;
import lombok.Data;
import org.okstar.cloud.defines.PayDefines;
import org.okstar.platform.common.datasource.SyncEntity;
import org.okstar.platform.common.datasource.domain.OkEntity;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
public class BillingOrder extends OkEntity implements SyncEntity {

    /**
     * 同步字段
     */
    private Boolean sync;

    /** 订单编号(与云端一致) */
    private String no;

    /** 订单状态 */
    private PayDefines.OrderStatus orderStatus;

    /** 支付状态 */
    private PayDefines.PaymentStatus paymentStatus;

    /** 金额 */
    private BigDecimal amount = new BigDecimal(0);

    /** 订单名称*/
    private String name;

    /** 附言 */
    private String memo;

    /** 到期时间 */
    private Date expire;

    private Boolean isExpired;

    /** 支付方式名称 */
    private String paymentName;

    /** 付款时间 */
    private Date paymentAt;

    /** 供应商 */
    private String providerName;

    /**
     * 开始时间
     */
    private Date periodBegin;

    /**
     * 结束时间
     */
    private Date periodEnd;
}
