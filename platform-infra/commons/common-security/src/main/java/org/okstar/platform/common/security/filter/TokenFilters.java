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

package org.okstar.platform.common.security.filter;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.logging.Log;
import io.quarkus.vertx.web.RouteFilter;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.okstar.platform.common.core.defined.SystemDefines;
import org.okstar.platform.common.string.OkStringUtil;


@ApplicationScoped
public class TokenFilters {

    @Inject
    JsonWebToken jwt;

    @RouteFilter(100)
    void jwtFilter(RoutingContext rc) {
        String uri = rc.request().uri();
        Log.infof("jwtFilter: %s", uri);

        if (uri.contains("/passport") || uri.contains("/rpc") || uri.contains("/_well-known") || uri.contains("/staff")) {
            Log.infof("bypass the uri.");
            rc.next();
            return;
        }

        Log.infof("jwt: %s", jwt);
        String username = jwt.getName();
        if (OkStringUtil.isEmpty(username)) {
            Log.warnf("Access denied!");
            rc.fail(HttpResponseStatus.FORBIDDEN.code());
            return;
        }

        Log.infof("username:%s", username);
        rc.put(SystemDefines.Header_X_OK_username, username);
        rc.next();
    }
}
