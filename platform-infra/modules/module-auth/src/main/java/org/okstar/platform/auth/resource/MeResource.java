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

package org.okstar.platform.auth.resource;

import io.quarkus.logging.Log;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.vo.SysAccount0;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Slf4j
@Path("me")
public class MeResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @GET
    public Res<SysAccount0> get() {
        for (var name : jwt.getClaimNames()) {
            Log.infof("claim=>%s %s", name, jwt.claim(name));
        }
        String name = jwt.getName();
        Log.infof("name:%s", name);
        var result = RpcAssert.isTrue(sysAccountRpc.findByUsername(name));
        Log.infof("My info is:%s", result);
        return Res.ok(Req.empty(), result);
    }
}
