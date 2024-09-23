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
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.List;

@RegisterRestClient
@Path("rpc/SysPropertyRpc")
public interface SysPropertyRpc {

    @POST
    @Path("save")
    void save(SysPropertyDTO sysPropertyDTO);

    @DELETE
    @Path("delete/{group}")
    void deleteByGroup(@PathParam("group") String group);

    @GET
    @Path("getByGroup/{group}")
    List<SysPropertyDTO> getByGroup(@PathParam("group") String group);

    @GET
    @Path("getByKey/{group}/{domain}/{key}")
    List<SysPropertyDTO> getByKey(@PathParam("group") String group,
                                  @PathParam("domain") String domain,
                                  @PathParam("key") String key);

}
