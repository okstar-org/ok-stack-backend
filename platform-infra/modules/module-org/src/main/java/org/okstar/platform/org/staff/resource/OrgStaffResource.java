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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.core.web.resource.OkCommonResource;
import org.okstar.platform.org.staff.domain.OrgStaff;
import org.okstar.platform.org.staff.service.OrgStaffService;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.rpc.SysProfileRpc;

import java.util.List;

/**
 * 人员接口
 */
@Path("staff")
public class OrgStaffResource extends OkCommonResource {

    @Inject
    OrgStaffService orgStaffService;
    @Inject @RestClient
    SysProfileRpc sysProfileRpc;

    @GET
    @Path("findByDept/{deptId}")
    public Res<List<OrgStaff>> findByDeptId(@PathParam("deptId") Long deptId) {
        List<OrgStaff> list = orgStaffService.children(deptId);
        return Res.ok(list);
    }

    @PUT
    @Path("/{accountId}")
    public Res<Long> save(@PathParam("accountId") Long accountId, SysProfileDTO req) {
        var added = sysProfileRpc.save(accountId, req);
        return Res.ok(added);
    }

    @GET
    @Path("count")
    public Res<Long> count() {
        var count = orgStaffService.getCount();
        return Res.ok(count);
    }
}
