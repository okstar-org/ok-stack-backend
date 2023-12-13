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

package org.okstar.platform.system.account.resource;

import io.quarkus.security.Authenticated;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.system.account.domain.SysProfile;
import org.okstar.platform.system.account.service.SysProfileService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

@Authenticated
@Path("/profile")
public class SysProfileResource extends OkCommonResource {

    @Inject
    SysProfileService profileService;

    @GET
    public Res<SysProfile> get() {
        String username = getUsername();
        var profile = profileService.loadByAccountId(username);
        return Res.ok(Req.empty(), profile);
    }

    @PUT
    public Res<Boolean> put(SysProfile profile){
        profileService.save(profile);
        return Res.ok(true);
    }
}
