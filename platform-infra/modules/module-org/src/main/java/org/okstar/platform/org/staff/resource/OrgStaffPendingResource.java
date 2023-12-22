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

package org.okstar.platform.org.staff.resource;

import jakarta.inject.Inject;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.org.domain.OrgStaff;
import org.okstar.platform.org.staff.service.OrgStaffPostService;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.org.vo.OrgStaffJoinReq;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * 组织架构-人员管理-待入职
 */
@Path("staff/pending")
public class OrgStaffPendingResource {

    @Inject
    OrgStaffService orgStaffService;
    @Inject
    OrgStaffPostService staffPostService;


    @GET
    @Path("page")
    public Res<List<OrgStaff>> page() {
        var list = orgStaffService.findPendings();
        return Res.ok(Req.empty(), list);
    }


    /**
     * 入职
     */
    @POST
    @Path("join")
    public Res<Boolean> join(OrgStaffJoinReq req) {
        var yes = staffPostService.join(req.getStaffId(), req.getPostIds());
        return Res.ok(req, yes);
    }
}
