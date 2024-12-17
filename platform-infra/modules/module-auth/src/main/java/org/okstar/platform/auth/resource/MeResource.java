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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.okstar.platform.auth.dto.Me;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.system.dto.SysAccountDTO;

/**
 * 读取自己信息接口
 */
@Authenticated
@Path("me")
public class MeResource extends BaseResource {

    @GET
    public Res<Me> get() {
        SysAccountDTO account0 = self();
        Me me = Me.builder().account(account0).build();
        Log.infof("Me: %s", me);
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
