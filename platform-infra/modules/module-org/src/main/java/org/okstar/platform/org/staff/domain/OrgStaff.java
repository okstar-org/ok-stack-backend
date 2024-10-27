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

package org.okstar.platform.org.staff.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.okstar.platform.core.org.JobDefines;
import org.okstar.platform.org.domain.BaseEntity;

import java.util.Date;


/**
 * 组织-人员
 */
@Setter
@Getter
@ToString(callSuper = true)
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"accountId"})})
public class OrgStaff extends BaseEntity {

    /**
     * 帐号ID
     */
    private Long accountId;

    /**
     * 编号
     */
    private String no;

    /**
     * 入职日期
     */
    private Date joinedDate;

    /**
     * 离职日期
     */
    private Date leftDate;

    /**
     * 岗位状态
     */
    @Enumerated(EnumType.STRING)
    private JobDefines.PostStatus postStatus;
}
