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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 组织
 * 
 * 
 */
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "no"})})
public class Org extends BaseEntity
{

    /** 上级组织 */
    private Long parentId;

    /**
     * 组织地址
     * 配置信息: {url}/org/.well-known/configuration
     */
    private String url;

    /**
     * 当前组织
     */
    private Boolean current;
}