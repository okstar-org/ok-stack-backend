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

package org.okstar.platform.system.settings.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.system.resource.BaseResource;
import org.okstar.platform.system.settings.domain.SysConfIntegration;
import org.okstar.platform.system.settings.domain.SysConfIntegrationIm;
import org.okstar.platform.system.settings.domain.SysConfIntegrationKeycloak;
import org.okstar.platform.system.settings.domain.SysConfIntegrationStack;
import org.okstar.platform.system.settings.service.SysConfIntegrationService;

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
        integrationService.saveStack(conf);
        return Res.ok(true);
    }

    @POST
    @Path("/stack/test")
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
    @Path("/keycloak/test")
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
    @Path("/im/test")
    public Res<Boolean> testIm(SysConfIntegrationIm conf) {
        boolean y = integrationService.testIm(conf);
        return Res.ok(y);
    }
}
