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
package org.okstar.platform.billing.rpc;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.billing.dto.OrderDTO;
import org.okstar.platform.common.rpc.RpcResult;

import java.util.List;

@Path("rpc/BillingOrderRpc")
@RegisterRestClient
public interface BillingOrderRpc {


    @GET
    @Path("get/{id}")
    RpcResult<OrderDTO> get(@PathParam("id") Long id);

    @GET
    @Path("list")
    RpcResult<List<OrderDTO>> list();
}
