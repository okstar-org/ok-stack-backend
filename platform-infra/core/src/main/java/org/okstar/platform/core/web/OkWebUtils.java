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

package org.okstar.platform.core.web;

import io.vertx.mutiny.core.http.Cookie;
import io.vertx.mutiny.core.http.HttpServerRequest;

public class OkWebUtils {
    public static String getIpAddr(HttpServerRequest request) {
        if (request == null) {
            return null;
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null) {
            if (!ip.contains(",")) {
                return ip;

            }
            String[] ips = ip.split(",");
            return ips[ips.length-1];
        }

        return null;
    }

    public static String getValue(HttpServerRequest request, String name) {
        String param = request.getParam(name);
        if (param != null)
            return param;

        String header = request.getHeader(name);
        if (header != null)
            return header;

        Cookie cookie = request.getCookie(name);
        if (cookie != null) return cookie.getValue();

        return null;
    }

}
