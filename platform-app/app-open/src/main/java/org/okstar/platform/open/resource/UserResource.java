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
import ok.okstar.stack.api.dto.UserDTO;
import ok.okstar.stack.api.dto.UserDTOs;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.rpc.SysUserRpc;

import java.util.List;

/**
 * 开放平台-帐号信息
 */
@Path("user")
public class UserResource {

    @Inject
    @RestClient
    SysUserRpc userRpc;

    @GET
    @Path("search")
    public UserDTOs search(@QueryParam("q") String query) {
        List<SysAccountDTO> list = userRpc.search(query);
        UserDTOs d = new UserDTOs();
        d.setData(list.stream().map(e->{
            var d1 = new UserDTO();
            d1.setUsername(e.getUsername());
            return d1;
        }).toList());
        return d;
    }
}
