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
import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.dto.OrgDeptAdd;
import org.okstar.platform.org.service.OrgDeptService;
import org.okstar.platform.org.service.OrgService;
import org.okstar.platform.system.rpc.SysAccountRpc;

import java.util.List;
import java.util.Optional;

@Path("dept")
public class OrgDeptResource extends OkCommonResource {
    @Inject
    OrgDeptService deptService;
    @Inject
    OrgService orgService;
    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @POST
    @Path("add/{parentId}")
    public Res<List<OrgDept>> add(@PathParam("parentId") Long parentId, OrgDeptAdd add) {

        var parent = deptService.get(parentId);
        OkAssert.isTrue(parent != null, "参数异常！");

        Optional<Org> current = orgService.current();

        String username = getUsername();
        var account0 = RpcAssert.isTrue(sysAccountRpc.findByUsername(username));
        add.setOrgId(current.get().id);
        add.setParentId(parentId);
        add.setLevel(parent.getLevel()+1);
        deptService.add(account0.getId(), add);

        return Res.ok(Req.empty());
    }


    @GET
    @Path("children")
    public Res<List<OrgDept>> orgChildren() {
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

    @GET
    @Path("count")
    public Res<Long> count() {
        var count = deptService.getCount();
        return Res.ok(count);
    }

    @DELETE
    @Path("deleteById/{id}")
    public Res<Boolean> deleteById(@PathParam("id") Long id) {
        deptService.deleteById(id);
        return Res.ok(true);
    }
}
