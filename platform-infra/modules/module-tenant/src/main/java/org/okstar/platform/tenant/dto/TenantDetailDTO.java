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

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.okstar.platform.common.core.web.bean.DTO;
import org.okstar.platform.tenant.defines.TenantDefined;
import org.okstar.platform.tenant.doc.TenantMetaDoc;

@Data
@EqualsAndHashCode(callSuper = true)
public class TenantDetailDTO extends DTO {

    private Long id;

    /**
     * 编号
     */
    private String no;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态
     */
    private TenantDefined.TenantStatus status;

    private TenantMetaDoc meta;
}
