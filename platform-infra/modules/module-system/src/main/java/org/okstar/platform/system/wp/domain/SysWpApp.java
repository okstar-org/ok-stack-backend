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

package org.okstar.platform.system.wp.domain;

import lombok.Data;
import org.okstar.platform.system.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 系统管理-工作平台-应用管理
 */
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"app_key"})})
public class SysWpApp extends BaseEntity {

    /**
     * Key
     */
    @Column(name = "app_key", nullable = false)
    private String key;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 图标
     */
    private String avatar;

    /**
     * 备注
     */
    private String descr;

    /**
     * 开发者
     */
    private String author;

    /**
     * 邮件
     */
    private String mail;

    /**
     * 主页
     */
    private String homePage;
}
