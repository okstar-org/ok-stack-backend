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

package org.okstar.platform.org.domain;

import lombok.Data;
import org.okstar.platform.common.datasource.domain.OkEntity;

import jakarta.persistence.MappedSuperclass;

/**
 * Entity基类
 * 
 * 
 */
@Data
@MappedSuperclass
public class BaseEntity extends OkEntity
{



    /**
     * 禁用
     */
    private Boolean disabled;

    public Boolean getDisabled() {
        return disabled == null || disabled;
    }
}
