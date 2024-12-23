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
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.org.dto.OrgPost0;
import org.okstar.platform.org.service.OrgPostService;

import java.util.List;

/**
 * 人员岗位接口
 */
@Path("staff/post")
public class OrgStaffPostResource {

    @Inject
    OrgPostService orgPostService;

    @GET
    @Path("list")
    public Res<List<OrgPost0>> list(@QueryParam( "assignment") @DefaultValue("false") Boolean assignment) {
        var list = orgPostService.findAssignAble(assignment, false);
        return Res.ok(list);
    }

}
