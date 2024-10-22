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

package org.okstar.platform.system.conf.resource;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.system.conf.domain.*;
import org.okstar.platform.system.conf.service.SysConfIntegrationService;
import org.okstar.platform.system.resource.BaseResource;

@Path("conf/integration")
public class SysConfIntegrationResource extends BaseResource {

    @Inject
    SysConfIntegrationService integrationService;


    @GET
    @Path("")
    public Res<SysConfIntegration> get() {
        return Res.ok(integrationService.find());
    }

    @PUT
    @Path("")
    public void put(SysConfIntegration integration) {
        integrationService.save(integration);
    }

    @PUT
    @Path("stack")
    public Res<Boolean> putStack(SysConfIntegrationStack conf) {
        integrationService.save(conf);
        return Res.ok(true);
    }

    @POST
    @Path("test/stack")
    public Res<Boolean> testStack(SysConfIntegrationStack conf) {
        boolean y = integrationService.testStack(conf);
        return Res.ok(y);
    }

    @PUT
    @Path("keycloak")
    public Res<Boolean> putKeycloak(SysConfIntegrationKeycloak conf) {
        integrationService.saveKeycloak(conf);
        return Res.ok(true);
    }


    @POST
    @Path("/test/keycloak")
    public Res<Boolean> testKeycloak(SysConfIntegrationKeycloak conf) {
        boolean y = integrationService.testKeycloak(conf);
        return Res.ok(y);
    }

    @PUT
    @Path("im")
    public Res<Boolean> putIm(SysConfIntegrationIm conf) {
        integrationService.saveIm(conf);
        return Res.ok(true);
    }

    @POST
    @Path("/test/im")
    public Res<Boolean> testIm(SysConfIntegrationIm conf) {
        conf.setPort(5222);
        boolean y = integrationService.testIm(conf);
        return Res.ok(y);
    }

    @PUT
    @Path("minio")
    public Res<Boolean> putConf(SysConfIntegrationMinio conf) {
        Log.infof("save config: %s", conf);
        integrationService.save(conf);
        return Res.ok(true);
    }

    @POST
    @Path("/test/minio")
    public Res<Boolean> testConf(SysConfIntegrationMinio conf) {
        Log.infof("test config: %s", conf);
        boolean y = integrationService.testMinio(conf);
        return Res.ok(y);
    }
}
