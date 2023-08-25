/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
 * OkEDU-Classroom is licensed under Mulan PubL v2.
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

import io.vertx.core.http.HttpServerRequest;
import org.okstar.platform.auth.form.LoginBody;
import org.okstar.platform.auth.service.SysLoginService;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.core.web.controller.OkBaseController;
import org.okstar.platform.common.security.domain.LoginUser;
import org.okstar.platform.common.security.service.TokenService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;


/**
 * token 控制
 *
 *  
 */
@Path("token")
public class TokenController extends OkBaseController {

    @Inject
    private TokenService tokenService;

    @Inject
    private SysLoginService sysLoginService;


    @POST
    @Path("login")
    public Res<?> login( LoginBody form) {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form);
        // 获取登录token
        return Res.ok(tokenService.createToken(request, userInfo));
    }

    @GET
    @Path("logout")
    public Res<?> logout(HttpServerRequest request) {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (OkStringUtil.isNotNull(loginUser)) {
            String username = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            sysLoginService.logout(username);
        }
        return Res.ok();
    }

    @POST @Path("refresh")
    public Res<?> refresh() {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (OkStringUtil.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return Res.ok();
        }
        return Res.ok();
    }
}
