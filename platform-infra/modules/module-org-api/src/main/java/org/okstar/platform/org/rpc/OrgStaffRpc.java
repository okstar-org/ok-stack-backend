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

package org.okstar.platform.org.rpc;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.org.dto.OrgStaff0;
import org.okstar.platform.org.dto.OrgStaffFragment;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.util.List;

@Path("rpc/OrgStaffRpc")
@RegisterRestClient
public interface OrgStaffRpc {

    @POST
    @Path("add")
    RpcResult<Boolean> add(OrgStaffFragment staffFragment);

    @GET
    @Path("search")
    RpcResult<List<OrgStaff0>> search(@QueryParam("q") String query);
}
