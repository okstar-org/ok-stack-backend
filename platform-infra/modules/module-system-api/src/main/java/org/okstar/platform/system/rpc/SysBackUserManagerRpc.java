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
import org.okstar.platform.system.dto.BackUser;

import java.util.Optional;

@Path("rpc/SysBackUserManagerRpc")
@RegisterRestClient
public interface SysBackUserManagerRpc {

    @POST
    @Path("add")
    BackUser add(BackUser user);

    @DELETE
    @Path("delete/{username}")
    boolean delete(@PathParam("username") String username);

    @GET
    @Path("get/{username}")
    Optional<BackUser> get(@PathParam("username") String username);

    @PUT
    @Path("resetPassword/{username}")
    void resetPassword(@PathParam("username") String username, String password);

    @GET
    @Path("forgot/{username}")
    void forgot(@PathParam("username") String username);
}

