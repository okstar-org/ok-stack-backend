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
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.system.resource.BaseResource;
import org.okstar.platform.system.settings.domain.SysConfIntegration;
import org.okstar.platform.system.settings.service.SysConfIntegrationService;

@Path("conf/integration")
public class SysConfIntegrationResource extends BaseResource {

    @Inject
    SysConfIntegrationService service;


    @GET
    @Path("")
    public Res<SysConfIntegration> get() {
        return Res.ok(service.find());
    }

    @PUT
    @Path("")
    public void put(SysConfIntegration integration) {
        service.save(integration);
    }
}
