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

package org.okstar.platform.system.account.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.core.web.resource.OkCommonResource;
import org.okstar.platform.system.conf.domain.SysConfWebsite;
import org.okstar.platform.system.conf.service.SysConfSettingsService;

@Path("open")
public class OpenResource extends OkCommonResource {

    @Inject
    SysConfSettingsService sysConfSettingsService;

    @GET
    @Path("website")
    public Res<SysConfWebsite> website() {
        return Res.ok(sysConfSettingsService.loadWebsite());
    }
}
