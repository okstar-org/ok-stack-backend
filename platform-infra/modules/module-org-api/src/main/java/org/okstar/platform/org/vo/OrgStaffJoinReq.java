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

package org.okstar.platform.org.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.okstar.platform.common.web.bean.Req;

import java.util.SortedSet;

/**
 * 员工入职请求
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrgStaffJoinReq extends Req {

    /**
     * 人员ID
     */
    private Long staffId;

    /**
     * 岗位ID
     */
    private SortedSet<Long> postIds;

}
