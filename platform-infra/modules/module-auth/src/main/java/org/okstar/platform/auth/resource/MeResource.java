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

package org.okstar.platform.auth.resource;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.cloud.entity.FederalCitizenEntity;
import org.okstar.platform.auth.dto.Me;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.org.rpc.OrgRpc;
import org.okstar.platform.system.dto.SysProfileDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.rpc.SysProfileRpc;
import org.okstar.platform.system.vo.SysAccount0;

import java.util.concurrent.ExecutorService;

@Slf4j
@Path("me")
public class MeResource extends BaseResource {

    @Inject
    ExecutorService executorService;

    @Inject
    @RestClient
    SysAccountRpc sysAccountRpc;

    @Inject
    @RestClient
    SysProfileRpc sysProfileRpc;

    @Inject
    @RestClient
    OrgRpc orgRpc;

    OkCloudApiClient client;

    public MeResource() {
        client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API_STACK,
                new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME, OkCloudDefines.OK_CLOUD_PASSWORD));
    }


    @GET
    public Res<Me> get() {
//        for (var name : jwt.getClaimNames()) {
//            Log.infof("claim=>%s %s", name, jwt.claim(name));
//        }
//        String name = jwt.getName();
//        Log.infof("name:%s", name);
        SysAccount0 account0 = self();
        SysProfileDTO profileDTO = sysProfileRpc.getByAccount(account0.getId());

//      var account0 = RpcAssert.isTrue(sysAccountRpc.findByUsername(sysAccount0.getUsername()));

        executorService.execute(() -> {
            detach(account0);
        });

        Me me = Me.builder().account(account0).profile(profileDTO).build();
        Log.infof("My info is:%s", me);
        return Res.ok(me);
    }

    private void detach(SysAccount0 account0) {

        FederalChannel channel = client.getFederalChannel();
        FederalCitizenEntity ex = new FederalCitizenEntity();
        ex.setAccountId(String.valueOf(account0.getId()));

        try {
            var org0 = RpcAssert.isTrue(orgRpc.current());
            ex.setStateCert(org0.getCert());
        } catch (Exception ignored) {
        }

        String cert = channel.registerCitizen(ex);
        Log.infof("Account cert=>%s", cert);
        if (cert != null) {
            sysAccountRpc.setCert(account0.getId(), cert);
        }
    }
}
