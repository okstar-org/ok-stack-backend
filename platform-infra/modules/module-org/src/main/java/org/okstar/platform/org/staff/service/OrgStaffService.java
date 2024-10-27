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


import org.okstar.platform.common.web.page.OkPageResult;
import org.okstar.platform.common.web.page.OkPageable;
import org.okstar.platform.common.datasource.OkJpaService;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.dto.OrgEmployee;
import org.okstar.platform.org.staff.dto.OrgStaffDTO;
import org.okstar.platform.org.vo.OrgStaffFind;
import org.okstar.platform.system.dto.SysProfileDTO;

import java.util.List;
import java.util.Optional;

/**
 * 部门管理 服务层
 */
public interface OrgStaffService extends OkJpaService<OrgStaff> {

    List<OrgStaff> children(Long parentId);

    /**
     * 查找[待入职]人员
     *
     * @return
     */
    OkPageResult<OrgStaff> findPendings(OkPageable pageable);

    /**
     * 查找[已入职]人员
     * @param find
     * @return
     */
    OkPageResult<OrgEmployee>  findEmployees(OrgStaffFind find);

    List<OrgEmployee> toEmployees(List<OrgStaff> pq);

    OrgEmployee toEmployee(OrgStaff staff);

    /**
     * 查找[已离职]人员
     *
     * @return
     */
    OkPageResult<OrgStaffDTO> findLefts(OkPageable page);
    
    void setAccountId(Long id, Long accountId);

    Optional<OrgStaff> getByAccountId(Long id);

    List<OrgEmployee> search(String query);

    long getCount();

    /**
     * 设置帐号profile信息到员工
     * @param dto
     */
    void save(SysProfileDTO dto);


}
