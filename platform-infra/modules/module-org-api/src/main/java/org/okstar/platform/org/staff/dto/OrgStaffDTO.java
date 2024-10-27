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

package org.okstar.platform.org.staff.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.okstar.platform.core.org.JobDefines;
import org.okstar.platform.system.dto.SysProfileDTO;

import java.util.Date;

@Data
public class OrgStaffDTO {
    /**
     * ID
     */
    private Long id;

    protected Date createAt;

    protected Date updateAt;

    /**
     * 帐号ID
     */
    protected Long accountId;

    /**
     * 编号
     */
    protected String no;

    /**
     * 入职日期
     */
    protected Date joinedDate;

    /**
     * 离职日期
     */
    protected Date leftDate;

    /**
     * 岗位状态
     */
    @Enumerated(EnumType.STRING)
    private JobDefines.PostStatus postStatus;

    /**
     * 详情
     */
    private SysProfileDTO profile;
}
