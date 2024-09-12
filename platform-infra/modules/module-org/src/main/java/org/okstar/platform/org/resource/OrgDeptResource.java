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

package org.okstar.platform.org.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.dto.OrgDeptAdd;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgService;

import java.util.List;

@Path("dept")
public class OrgDeptResource extends BaseResource {
    @Inject
    OrgDeptService deptService;
    @Inject
    OrgService orgService;

    @POST
    @Path("{parentId}")
    public Res<Long> create(@PathParam("parentId") Long parentId, OrgDeptAdd add) {

        OkAssert.isTrue(parentId != null, "参数异常！");

        var parent = deptService.get(parentId);
        OkAssert.isTrue(parent != null, "参数异常！");

        Org org = orgService.loadCurrent();
        OkAssert.isTrue(org != null, "未初始化组织！");

        var self = self();
        OrgDept t = new OrgDept();
        t.setName(add.getName());
        t.setNo(add.getNo());
        t.setDisabled(add.getDisabled());
        //组织
        t.setOrgId(org.id);
        //父级
        t.setParentId(parent.id);
        //级别+1
        t.setLevel(parent.getLevel() + 1);

        deptService.create(t, self.getId());

        return Res.ok(t.id);
    }

    @PUT
    @Path("{id}")
    public Res<Boolean> update(@PathParam("id") Long id, OrgDeptAdd add) {

        var dept = deptService.get(id);
        OkAssert.isTrue(dept != null, "参数异常！");

        dept.setNo(add.getNo());
        dept.setName(add.getName());
        dept.setDisabled(add.getDisabled());

        deptService.update(dept, self().getId());
        return Res.ok(true);
    }

    @GET
    @Path("children")
    public Res<List<OrgDept>> children() {
        Org current = orgService.loadCurrent();
        OkAssert.isTrue(current != null, "未初始化组织！");
        List<OrgDept> list = deptService.loadRootByOrgId(current.id);
        return Res.ok(Req.empty(), list);
    }

    @GET
    @Path("children/{parentId}")
    public Res<List<OrgDept>> children(@PathParam("parentId") Long parentId) {
        List<OrgDept> list = deptService.children(parentId);
        return Res.ok(Req.empty(), list);
    }

    @GET
    @Path("count")
    public Res<Long> count() {
        var count = deptService.getCount();
        return Res.ok(count);
    }

    @GET
    @Path("findById/{id}")
    public Res<OrgDept> findById(@PathParam("id") Long id) {
        var dept = deptService.get(id);
        return Res.ok(dept);
    }

    @DELETE
    @Path("deleteById/{id}")
    public Res<Boolean> deleteById(@PathParam("id") Long id) {
        deptService.deleteById(id);
        return Res.ok(true);
    }
}
