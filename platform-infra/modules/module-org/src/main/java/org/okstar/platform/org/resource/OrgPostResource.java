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

package org.okstar.platform.org.resource;

import jakarta.inject.Inject;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.domain.OrgStaff;
import org.okstar.platform.org.service.OrgPostService;
import org.okstar.platform.org.staff.service.OrgStaffService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("post")
public class OrgPostResource {
    @Inject
    OrgPostService orgPostService;

    @Inject
    OrgStaffService orgStaffService;


    @GET
    @Path("findByDept/{deptId}")
    public Res<List<OrgPost>> findByDept(@PathParam("deptId") Long deptId) {
        List<OrgPost> list = orgPostService.findByDept(deptId);
        list.stream().filter(e-> OkStringUtil.isNotEmpty(e.getAssignFor())).forEach(post -> {
            OrgStaff staff = (orgStaffService.get(Long.valueOf(post.getAssignFor())));
            if (staff != null) {
                post.setAssignFor(staff.getFragment().getName());
            }
        });
        return Res.ok(Req.empty(), list);
    }

    @POST
    @Path("save")
    public Res<List<OrgPost>> save(OrgPost post) {
        orgPostService.save(post);
        return Res.ok(null);
    }


}
