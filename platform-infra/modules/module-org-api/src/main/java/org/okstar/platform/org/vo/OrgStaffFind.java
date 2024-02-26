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

package org.okstar.platform.org.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.okstar.platform.common.core.defined.JobDefines;
import org.okstar.platform.common.core.web.page.OkPageable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OrgStaffFind extends OkPageable {

    /**
     * 岗位状态
     */
    JobDefines.PostStatus postStatus;

    /**
     * 部门ID
     */
    Long deptId;

}
