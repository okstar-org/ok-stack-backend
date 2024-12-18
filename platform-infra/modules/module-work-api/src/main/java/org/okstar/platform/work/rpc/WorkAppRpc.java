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

package org.okstar.platform.work.rpc;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.work.dto.AppDTO;
import org.okstar.platform.work.dto.AppMetaDTO;

import java.util.List;

/**
 * App应用RPC接口
 */
@RegisterRestClient
@Path("rpc/WorkAppRpc")
public interface WorkAppRpc {

    /**
     * 应用列表
     * @return List<AppDTO>
     */
    @GET
    @Path("list")
    RpcResult<List<AppDTO>> list();

    /**
     * 获取应用元数据
     * @param uuid 应用uuid
     * @return AppMetaDTO
     */
    @GET
    @Path("{uuid}/meta")
    RpcResult<AppMetaDTO> meta(@PathParam("uuid") String uuid);
}
