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


import jakarta.persistence.*;
import lombok.Data;
import org.okstar.platform.common.core.defined.JobDefines;
import org.okstar.platform.org.dto.OrgStaffFragment;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * 组织-人员
 */
@Data
@Table
@Entity
public class OrgStaff extends BaseEntity {

    private Long accountId;

    @Embedded
    private OrgStaffFragment fragment;

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


    @Transient
    private List<String> postNames = new LinkedList<>();

    @Transient
    private List<Long> postIds = new LinkedList<>();

}
