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

package org.okstar.platform.org.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 组织-岗位
 * 
 * 
 */
@Setter
@Getter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "no"})})
public class OrgPost extends BaseEntity
{
    /**
     * 编号
     */
    private String no;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String descr;

    /**
     * 所在部门
     */
    private Long deptId;

    /**
     * 分配给（是否分配）
     */
    private String assignFor;

    /**
     * 招聘链接
     */
    private String recruit;
}
