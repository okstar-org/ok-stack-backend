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

package org.okstar.platform.system.work.resource;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.entity.AppDetailEntity;
import org.okstar.cloud.entity.AppEntities;
import org.okstar.cloud.entity.AppEntity;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.resource.OkCommonResource;

@Authenticated
@Path("/work/app")
public class SysWorkAppResource extends OkCommonResource {

    OkCloudApiClient client;

    public SysWorkAppResource() {
        client = new OkCloudApiClient("http://localhost:1024/open/stack",
                new AuthenticationToken("okstar", "okstar.123#"));
    }

    @POST
    @Path("page")
    public Res<AppEntities> page(OkPageable pageable) {
        AppEntities apps = client.getAppChannel().getApps(pageable);
        return Res.ok(apps);
    }

    @GET
    @Path("{id}")
    public Res<AppEntity> get(@PathParam("id") Long id) {
        var page = client.getAppChannel().getApp(id);
        return Res.ok(page);
    }

    @GET
    @Path("/detail/{id}")
    public Res<AppDetailEntity> detail(@PathParam("id") Long id) {
        var app = client.getAppChannel().getDetail(id);
        return Res.ok(app);
    }
}
