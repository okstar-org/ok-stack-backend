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
import org.okstar.platform.common.core.defined.JobDefines;
import org.okstar.platform.common.core.defined.UserDefines;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;


/**
 * 组织-人员
 */
@Data
@Table
@Entity
public class OrgStaff extends BaseEntity {


    private Long accountId;

    /**
     * 名称
     */
    private String name;

    /**
     * 性别
     */
    private UserDefines.Gender gender;

    /**
     * 身份证ID
     */
    private String identity;

    /**
     * ISO国家代号
     * @link https://www.iso.org/obp/ui/#search
     */
    private String iso;

    /**
     * 电话
     */
    private String phone;

    /**
     * 备注
     */
    private String descr;

    /**
     * 生日
     */
    private Date birthday;

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
