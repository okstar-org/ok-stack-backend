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

package org.okstar.platform.work.web;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.okstar.cloud.entity.AppDetailEntity;
import org.okstar.cloud.entity.AppEntities;
import org.okstar.cloud.entity.AppEntity;
import org.okstar.cloud.entity.OkPageable;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.work.service.WorkAppService;

/**
 * 工作中心-应用列表
 */
@Authenticated
@Path("/app")
public class WorkAppResource extends BaseResource {

    @Inject
    WorkAppService workAppService;


    @POST
    @Path("page")
    public Res<AppEntities> page(OkPageable pageable) {
        return Res.ok(workAppService.page(pageable));
    }

    @GET
    @Path("{uuid}")
    public Res<AppEntity> get(@PathParam("uuid") String uuid) {
        return Res.ok(workAppService.get(uuid));
    }

    @GET
    @Path("/detail/{uuid}")
    public Res<AppDetailEntity> detail(@PathParam("uuid") String uuid) {
        return Res.ok(workAppService.detail(uuid));
    }
}
