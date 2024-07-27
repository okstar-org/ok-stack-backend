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


import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.datasource.OkJpaService;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.dto.OrgStaff0;
import org.okstar.platform.org.vo.OrgStaffFind;
import org.okstar.platform.org.vo.OrgStaffReq;
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
    OkPageResult<OrgStaff0>  findEmployees(OrgStaffFind find);

    /**
     * 查找[已离职]人员
     *
     * @return
     */
    OkPageResult<OrgStaff> findLefts(OkPageable page);

    /**
     * 添加员工，进入[待入职]模块
     *
     * @param req
     * @return
     */
    boolean add(OrgStaffReq req);

    void setAccountId(Long id, Long accountId);

    Optional<OrgStaff> getByAccountId(Long id);

    List<OrgStaff0> search(String query);

    long getCount();

    /**
     * 设置帐号profile信息到员工
     * @param dto
     */
    void save(SysProfileDTO dto);
}
