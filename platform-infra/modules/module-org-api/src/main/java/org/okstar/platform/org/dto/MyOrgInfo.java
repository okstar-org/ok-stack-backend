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

package org.okstar.platform.org.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.okstar.platform.common.core.web.bean.DTO;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.dto.SysAccountDTO;

import java.util.List;

/**
 * 我的组织信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyOrgInfo extends DTO {

    /**
     * 帐号信息
     */
    private SysAccountDTO account;

    /**
     * profile
     */
    private SysProfileDTO profile;

    /**
     * 组织信息
     */
    private Org0 org;

    /**
     * 员工信息
     */
    private OrgStaff0 staff;

    /**
     * 岗位信息
     */
    private List<MyPostInfo> postInfo;


}
