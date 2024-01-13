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

package org.okstar.platform.system.rpc;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;
import org.okstar.platform.system.vo.SysAccount0;

import jakarta.ws.rs.*;


@Path("rpc/SysAccountRpc")
@RegisterRestClient
public interface SysAccountRpc {

    @GET
    @Path("lastPassword/{accountId}")
    RpcResult<String> lastPassword(@PathParam("accountId") Long accountId);

    @POST
    @Path("signUp")
    RpcResult<SignUpResult> signUp(SignUpForm signUpDto);

    @DELETE
    @Path("signDown/{accountId}")
    RpcResult<Boolean> signDown(@PathParam("accountId") Long accountId);

    @GET
    @Path("findByBind")
    RpcResult<SysAccount0> findByBind(@QueryParam("iso") String iso,
                                      @QueryParam("type") AccountDefines.BindType type,
                                      @QueryParam("bindValue") String bindValue);

    @GET
    @Path("findByUsername")
    RpcResult<SysAccount0> findByUsername(@QueryParam("username") String username);

    @GET
    @Path("findById")
    RpcResult<SysAccount0> findById(@QueryParam("id") Long id);


    @POST
    @Path("setCert/{id}")
    void setCert(@PathParam("id") Long id, String cert);
}
