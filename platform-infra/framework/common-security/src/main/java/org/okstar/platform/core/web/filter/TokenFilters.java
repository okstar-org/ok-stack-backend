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

package org.okstar.platform.core.web.filter;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.logging.Log;
import io.quarkus.vertx.web.RouteFilter;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.okstar.platform.common.security.cache.OkLogonUserCache;
import org.okstar.platform.common.security.domain.OkLogonUser;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.common.web.OkHttpDefines;


@ApplicationScoped
public class TokenFilters {

    @Inject
    JsonWebToken jsonWebToken;

    @Inject
    OkLogonUserCache logonUserCache;

    @RouteFilter(1)
    void filter(RoutingContext rc) {
        HttpServerRequest request = rc.request();
        String uri = request.uri();
        Log.infof("Filter method:%s uri:%s", request.method(), uri);

        String from = request.getHeader(OkHttpDefines.Header_X_OK_from);
        Log.infof("header from:%s", from);

        if (OkStringUtil.isNotEmpty(from)) {
            Log.infof("bypass the uri.");
            rc.next();
            return;
        }

        if (uri.contains("/passport")
                || uri.contains("/q/")
                || uri.contains("/rpc")
                || uri.contains("/_well-known")
                || uri.contains("/open")
                || uri.contains("/webjars")
                || uri.contains("/staff")   //TODO 客户端获取员工
        ) {
            Log.infof("bypass the uri.");
            rc.next();
            return;
        }

        String username = jsonWebToken.getName();
        Log.infof("username:%s", username);
        if (OkStringUtil.isEmpty(username)) {
            Log.warnf("Access denied!");
            rc.fail(HttpResponseStatus.FORBIDDEN.code());
            return;
        }

        OkLogonUser logonUser = logonUserCache.set(username, jsonWebToken);
        Log.infof("logon user is: %s", logonUser);

        rc.put(OkHttpDefines.Header_X_OK_username, username);
        rc.next();
    }
}
