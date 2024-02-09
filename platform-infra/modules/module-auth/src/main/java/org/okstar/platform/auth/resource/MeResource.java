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
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.cloud.entity.FederalCitizenEntity;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.org.rpc.OrgRpc;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.vo.SysAccount0;

import java.util.concurrent.ExecutorService;

@Slf4j
@Path("me")
public class MeResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;
    @Inject
    @RestClient
    OrgRpc orgRpc;
    @Inject
    ExecutorService executorService;

    OkCloudApiClient client;

    public MeResource() {
        client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API,
                new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME, OkCloudDefines.OK_CLOUD_PASSWORD));
    }


    @GET
    public Res<SysAccount0> get() {
        for (var name : jwt.getClaimNames()) {
            Log.infof("claim=>%s %s", name, jwt.claim(name));
        }
        String name = jwt.getName();
        Log.infof("name:%s", name);

        var account0 = RpcAssert.isTrue(sysAccountRpc.findByUsername(name));

        executorService.execute(() -> {
            detach(account0);
        });

        Log.infof("My info is:%s", account0);
        return Res.ok(Req.empty(), account0);
    }

    private void detach(SysAccount0 account0) {
        var org0 = RpcAssert.isTrue(orgRpc.current());

        FederalChannel channel = client.getFederalChannel();
        FederalCitizenEntity ex = new FederalCitizenEntity();
        ex.setAccountId(String.valueOf(account0.getId()));
        ex.setName(account0.getName());
        ex.setStateCert(org0.getCert());

        String cert = channel.registerCitizen(ex);
        Log.infof("Account cert=>", cert);
        if (cert != null) {
            sysAccountRpc.setCert(account0.getId(), cert);
        }
    }
}
