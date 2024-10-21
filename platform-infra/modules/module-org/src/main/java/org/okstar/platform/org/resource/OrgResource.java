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

package org.okstar.platform.org.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.service.FederalService;
import org.okstar.platform.org.service.OrgService;

/**
 * 组织
 */
@Path("")
public class OrgResource extends BaseResource {

    @Inject
    OrgService orgService;

    @Inject
    FederalService federalService;


    @GET
    @Path("current")
    public Res<Org0> current() {
        return Res.ok(orgService.loadCurrent0());
    }

    @PUT
    @Path("save")
    public Res<Boolean> save(Org0 org0) {
        Org saved = orgService.save(org0);
        federalService.save(saved);
        return Res.ok(saved != null);
    }

}
