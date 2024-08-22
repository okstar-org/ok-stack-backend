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

package org.okstar.platform.tenant.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.okstar.platform.common.core.web.bean.DTO;
import org.okstar.platform.tenant.defines.TenantDefined;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InstanceDTO extends DTO {
    private Long id;

    private String uuid;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 编号
     */
    private String no;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 订单编号
     *
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

    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    private TenantDefined.TenantStatus status;

    private List<String> urls;

    private List<String> volumes;

    public void addUrl(String url) {
        if (urls == null) {
            urls = new ArrayList<>();
        }
        urls.add(url);
    }

    public void addVolumes(String volume) {
        if (volumes == null) {
            volumes = new ArrayList<>();
        }
        this.volumes.add(volume);
    }
}
