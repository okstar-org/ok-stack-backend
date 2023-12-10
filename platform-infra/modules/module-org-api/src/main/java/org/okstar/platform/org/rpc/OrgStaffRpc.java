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

package org.okstar.platform.org.rpc;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.org.dto.OrgStaffFragment;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("rpc/OrgStaffRpc")
@RegisterRestClient
public interface OrgStaffRpc {

    @POST
    @Path("add")
    RpcResult<Boolean> add(OrgStaffFragment staffFragment);

}
