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

package org.okstar.platform.org.staff.service;


import org.okstar.platform.common.datasource.OkService;
import org.okstar.platform.org.domain.OrgStaffPost;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * 人员与岗位关联
 */
public interface OrgStaffPostService extends OkService<OrgStaffPost> {


    List<OrgStaffPost> findByPostIds(Set<Long> posIds);

    List<OrgStaffPost> findByStaffIds(Set<Long> staffIds);


    /**
     * 操作离职
     * @param staffId
     * @return
     */
    boolean leave(Long staffId);

    /**
     * 操作入职
     *
     * @param staffId
     * @param postIds
     * @return
     */
    boolean join(Long staffId, SortedSet<Long> postIds);

    /**
     * 查找员工的岗位
     * @param staffId
     * @return
     */
    List<OrgStaffPost> findByStaffId(Long staffId);
}
