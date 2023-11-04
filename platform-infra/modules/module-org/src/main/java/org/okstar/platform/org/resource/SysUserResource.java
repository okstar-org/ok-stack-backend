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

package org.okstar.platform.org.resource;

import io.quarkus.security.Authenticated;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.controller.OkBaseController;
import org.okstar.platform.org.account.SysAccount;
import org.okstar.platform.org.service.SysUserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.List;


/**
 * 用户信息
 */
@Path("/user")
@Authenticated
public class SysUserResource extends OkBaseController {

    @Inject
    SysUserService sysUserService;

    @GET
    @Path("findAll")
    public Res<List<SysAccount>> findAll(){
        List<SysAccount> all = sysUserService.findAll();
        return Res.ok(all);
    }

}
