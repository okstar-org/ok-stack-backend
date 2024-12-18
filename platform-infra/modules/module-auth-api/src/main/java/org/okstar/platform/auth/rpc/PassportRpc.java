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

package org.okstar.platform.auth.rpc;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;
import org.okstar.platform.system.dto.SysAccountDTO;

import jakarta.ws.rs.*;

/**
 * 接入层API规范
 */
@Path("rpc/PassportRpc")
@RegisterRestClient
public interface PassportRpc {

    /**
     * 注册
     * @param signUpDto
     * @return
     */
    @POST
    @Path("signUp")
    RpcResult<SignUpResult> signUp(SignUpForm signUpDto);


    /**
     * 注销
     * @param accountId
     * @return
     */
    @DELETE
    @Path("signDown/{accountId}")
    RpcResult<Boolean> signDown(@PathParam("accountId") Long accountId);

    /**
     * 获取帐号
     * @param account
     * @return
     */
    @GET
    @Path("getAccount/{account}")
    RpcResult<SysAccountDTO> getAccount(@PathParam("account") String account);
}
