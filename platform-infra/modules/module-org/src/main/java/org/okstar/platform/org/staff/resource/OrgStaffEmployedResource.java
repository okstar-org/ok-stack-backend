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

package org.okstar.platform.org.staff.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.org.dto.OrgStaff0;
import org.okstar.platform.org.staff.service.OrgStaffPostService;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.org.vo.OrgStaffFind;

/**
 * 组织架构-人员管理-待入职
 */
@Path("staff/employed")
public class OrgStaffEmployedResource {

    @Inject
    OrgStaffPostService staffPostService;
    @Inject
    OrgStaffService orgStaffService;


    @POST
    @Path("leave")
    public Res<Boolean> leave(Long staffId) {
        boolean yes = staffPostService.leave(staffId);
        return Res.ok(yes);
    }

    @POST
    @Path("page")
    public Res<OkPageResult<OrgStaff0>> page(OrgStaffFind find) {
        var list = orgStaffService.findEmployees(find);
        return Res.ok(list);
    }
}
