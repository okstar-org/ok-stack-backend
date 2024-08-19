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

package org.okstar.platform.tenant.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

/**
 * 租户应用实例
 * 实例=为客户选择“租户”选择“应用”下“订单”产生的服务
 *
 */
@Setter
@Getter
@Entity
public class InstanceEntity extends BaseEntity{

    private String no;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 应用ID
     * @see org.okstar.cloud.entity.AppEntity
     */
    private Long appId;

    /**
     * 订单编号
     * @see org.okstar.platform.billing.order.domain.BillingOrder
     */
    private Long orderId;

    /**
     * 实例名称=租户名称+应用名称+订单名词
     */
    private String name;

    /**
     * 实例描述
     */
    private String description;


}
