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

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.okstar.platform.system.dto.SysConfIntegrationDTO;

/**
 * 系统设置RPC
 */
@Path("rpc/SysConfIntegrationRpc")
@RegisterRestClient
public interface SysConfIntegrationRpc {

    /**
     * 获取全局配置
     */
    @GET
    @Path("getIntegrationConf")
    SysConfIntegrationDTO getIntegrationConf();

    /**
     * 更新配置
     */
    @PUT
    @Path("updateConf")
    void uploadConf();
}
