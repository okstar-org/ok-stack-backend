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


import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.okstar.platform.auth.keycloak.BackResourceDTO;
import org.okstar.platform.auth.keycloak.BackResourceManager;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;

@Path("resource")
public class ResourceResource extends BaseResource {

    @Inject
    BackResourceManager resourceManager;

    @POST
    @Path("page")
    public Res<OkPageResult<BackResourceDTO>> page(OkPageable pageable) {
        var list = resourceManager.page(pageable, null, null, null, null, null);
        return Res.ok(OkPageResult.build(list, 10, pageable.getPageSize()));
    }

}
