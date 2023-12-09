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

package org.okstar.platform.open.resource;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.auth.rpc.PassportRpc;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.system.vo.SysAccount0;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * 通行
 */
@Slf4j
@Path("passport")
public class PassportResource {

    @Inject
    @RestClient
    PassportRpc passportRpc;

    @GET
    @Path("account/{account}")
    public Res<SysAccount0> getAccount(@PathParam("account") String account) {
        var resultDto = passportRpc.getAccount(account);
        log.info("resultDto=>{}", resultDto);
        return Res.ok(resultDto.getData());
    }
}
