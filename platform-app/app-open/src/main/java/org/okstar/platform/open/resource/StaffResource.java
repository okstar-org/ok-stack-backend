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

package org.okstar.platform.open.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.core.rpc.RpcAssert;
import org.okstar.platform.org.dto.OrgStaff0;
import org.okstar.platform.org.rpc.OrgStaffRpc;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;

import java.util.List;


@Path("staff")
public class StaffResource {

    @Inject
    @RestClient
    OrgStaffRpc orgStaffRpc;

    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @GET
    @Path("search")
    public Res<List<OrgStaff0>> search(@QueryParam("q") String query) {
        var list = RpcAssert.isTrue(orgStaffRpc.search(query));
        list.forEach(e -> {
            if (e.getAccountId() != null) {
                SysAccountDTO account0 = RpcAssert.isTrue(sysAccountRpc.findById(e.getAccountId()));
                e.setUsername(account0.getUsername());
            }
        });
        return Res.ok(list);
    }
}
