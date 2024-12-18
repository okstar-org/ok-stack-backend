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


import jakarta.ws.rs.PUT;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.system.dto.SysProfileDTO;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * 个人信息RPC
 */
@RegisterRestClient
@Path("rpc/SysProfileRpc")
public interface SysProfileRpc {

    /**
     * 获取个人信息通过帐号ID
     * @param accountId 帐号ID
     * @return SysProfileDTO
     */
    @GET
    @Path("getByAccount/{accountId}")
    SysProfileDTO getByAccount(@PathParam("accountId") Long accountId);

    /**
     * 保存个人信息
     * @param accountId 帐号ID
     * @param req SysProfileDTO
     * @return Long
     */
    @PUT
    @Path("{accountId}")
    Long save(@PathParam("accountId") Long accountId, SysProfileDTO req);
}
