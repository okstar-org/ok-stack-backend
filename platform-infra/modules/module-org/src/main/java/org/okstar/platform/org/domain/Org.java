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

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;


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
    /**
     * 编号
     */
    private String no;

    /**
     * 名称
     */
    private String name;


    /** 上级组织 */
    private Long parentId;

    /**
     * 组织地址
     */
    private String url;

    /**
     * 组织位置
     */
    private String location;

    /**
     * 组织头像（Logo）
     */
    private String avatar;

    /**
     * 当前组织
     */
    private Boolean current;

    /**
     * 认证号
     */
    private String cert;
}
