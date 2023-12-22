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
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Optional;

@Path("dept")
public class OrgDeptResource {
    @Inject
    OrgDeptService deptService;
    @Inject
    OrgService orgService;



    @GET
    @Path("children")
    public Res<List<OrgDept>> childrenByOrg() {
        Optional<Org> current = orgService.current();
        OkAssert.isTrue(current.isPresent(), "未初始化组织！");
        List<OrgDept> list = deptService.getByOrgId(current.get().id);
        return Res.ok(Req.empty(), list);
    }

    @GET
    @Path("children/{parentId}")
    public Res<List<OrgDept>> children(@PathParam("parentId") Long parentId) {
        List<OrgDept> list = deptService.children(parentId);
        return Res.ok(Req.empty(), list);
    }


}
