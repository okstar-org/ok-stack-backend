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
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.auth.dto.Me;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.rpc.SysProfileRpc;

@Authenticated
@Path("me")
public class MeResource extends BaseResource {

//    @Inject
//    ExecutorService executorService;
//
//    @Inject
//    @RestClient
//    SysAccountRpc sysAccountRpc;

    @Inject
    @RestClient
    SysProfileRpc sysProfileRpc;


    @GET
    public Res<Me> get() {
        SysAccountDTO account0 = self();
//        SysProfileDTO profileDTO = sysProfileRpc.getByAccount(account0.getId());
        Me me = Me.builder().account(account0)
//                .profile(profileDTO)
                .build();
        Log.infof("My info is: %s", me);
        return Res.ok(me);
    }


//    OkCloudApiClient client;
//    public MeResource() {
//        client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API_STACK,
//                new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME, OkCloudDefines.OK_CLOUD_PASSWORD));
//    }
//    private void detach(SysAccountDTO account0) {
//
//        FederalChannel channel = client.getFederalChannel();
//        FederalCitizenEntity ex = new FederalCitizenEntity();
//        ex.setAccountId(String.valueOf(account0.getId()));
//
//        String cert = channel.registerCitizen(ex);
//        Log.infof("Account cert=>%s", cert);
//        if (cert != null) {
//            sysAccountRpc.setCert(account0.getId(), cert);
//        }
//    }
}
