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
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.vo.SysAccount0;

/**
 * 通行
 */
@Slf4j
@Path("passport")
public class PassportResource {

    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @GET
    @Path("account/{account}")
    public Res<SysAccount0> getAccount(@PathParam("account") String account) {
        var sysAccount0 = RpcAssert.isTrue(sysAccountRpc.findByAccount(account));
        return Res.ok(sysAccount0);
    }
}
