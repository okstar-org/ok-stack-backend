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

package org.okstar.platform.system.rpc;

import jakarta.ws.rs.*;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.system.dto.SysAccountBindDTO;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;

import java.util.List;


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
    @Path("findByAccount/{account}")
    RpcResult<SysAccountDTO> getByAccount(@PathParam("account") String account);

    @GET
    @Path("findByBind")
    RpcResult<SysAccountDTO> findByBind(@QueryParam("type") AccountDefines.BindType type,
                                        @QueryParam("bindValue") String bindValue);

    @GET
    @Path("findByEmail")
    RpcResult<SysAccountDTO> findByEmail(@QueryParam("type") AccountDefines.BindType type,
                                         @QueryParam("email") String email);

    @GET
    @Path("findByUsername")
    RpcResult<SysAccountDTO> findByUsername(@QueryParam("username") String username);

    @GET
    @Path("findById/{id}")
    RpcResult<SysAccountDTO> findById(@PathParam("id") Long id);


    @POST
    @Path("setCert/{id}")
    void setCert(@PathParam("id") Long id, String cert);

    @GET
    @Path("getBinds/{id}")
    RpcResult<List<SysAccountBindDTO>> getBinds(@PathParam("id") Long id);


}
