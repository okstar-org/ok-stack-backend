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

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.okstar.platform.core.web.resource.OkCommonResource;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.service.SysAccountService;

import java.util.List;
import java.util.Optional;


/**
 * 账号信息
 */
@Authenticated
@Path("/account")
public class SysAccountResource extends OkCommonResource {

    @Inject
    SysAccountService sysAccountService;

    @GET
    @Path("findAll")
    public Res<List<SysAccount>> findAll() {
        List<SysAccount> all = sysAccountService.findAll();
        return Res.ok(Req.empty(), all);
    }

    @GET
    @Path("username/{bindType}/{value}")
    public Res<String> getUsername(@PathParam("bindType") AccountDefines.BindType bindType,
                                   @PathParam("value") String value,
                                   @QueryParam("iso") String iso) {

        var account = sysAccountService.findByBind(
                bindType,
                Optional.ofNullable(iso).orElse(AccountDefines.DefaultISO),
                value);

        return Res.ok(account.map(SysAccount::getUsername).orElse(null)) ;
    }

}
