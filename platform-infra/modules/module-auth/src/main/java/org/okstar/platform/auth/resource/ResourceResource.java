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
import org.okstar.platform.common.web.bean.Res;

import java.util.List;

@Path("resource")
public class ResourceResource extends BaseResource {

    @Inject
    BackResourceManager resourceManager;

    @POST
    @Path("list")
    public Res<List<BackResourceDTO>> list() {
        var list = resourceManager.list();
        return Res.ok(list);
    }


}
