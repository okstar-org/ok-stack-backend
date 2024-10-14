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
import jakarta.ws.rs.Path;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.system.resource.BaseResource;
import org.okstar.platform.system.settings.service.SysKeycloakService;

@Path("oidc")
public class SysOidcResource extends BaseResource {

    @Inject
    SysKeycloakService oidcService;

    @GET
    @Path("test")
    public Res<String> test() {
        String tested = oidcService.testConfig();
        return Res.ok(tested);
    }
}
