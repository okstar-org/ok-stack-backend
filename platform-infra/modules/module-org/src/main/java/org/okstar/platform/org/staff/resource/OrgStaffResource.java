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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.org.vo.OrgStaffReq;

import java.util.List;

@Path("staff")
public class OrgStaffResource extends OkCommonResource {

    @Inject
    OrgStaffService orgStaffService;

    @GET
    @Path("findByDept/{deptId}")
    public Res<List<OrgStaff>> findByDeptId(@PathParam("deptId") Long deptId) {
        List<OrgStaff> list = orgStaffService.children(deptId);
        return Res.ok(list);
    }

    @POST
    @Path("save")
    public Res<Boolean> save(OrgStaffReq req) {
        var added = orgStaffService.add(req);
        return Res.ok(added);
    }

    @GET
    @Path("count")
    public Res<Long> count() {
        var count = orgStaffService.getCount();
        return Res.ok(count);
    }
}
