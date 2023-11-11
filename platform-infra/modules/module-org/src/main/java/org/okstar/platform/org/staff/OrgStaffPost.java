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

package org.okstar.platform.org.staff;

import org.okstar.platform.org.domain.BaseEntity;
import org.okstar.platform.org.domain.OrgPost;

/**
 * 人员和岗位关联
 * 
 *
 */
public class OrgStaffPost extends BaseEntity
{
    /** 人员 */
    private OrgStaff staff;
    
    /** 岗位 */
    private OrgPost post;

}
