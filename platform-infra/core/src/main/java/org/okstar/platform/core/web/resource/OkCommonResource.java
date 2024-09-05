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

package org.okstar.platform.core.web.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import org.okstar.platform.common.core.web.OkHttpDefines;

import java.io.InputStream;

public class OkCommonResource {

    public static final String GIT_PROPERTIES = "/git.properties";

    @Context
    protected HttpServerRequest req;
    @Context
    protected HttpServerResponse res;
    @Inject
    protected RoutingContext rc;
    @Inject
    protected ObjectMapper objectMapper;

    protected String getUsername() {
        return rc.get(OkHttpDefines.Header_X_OK_username);
    }

    protected JsonNode gitVersion() {
        try (InputStream stream = getClass().getResourceAsStream(GIT_PROPERTIES)) {
            return objectMapper.readTree(stream);
        } catch (Exception e) {
            Log.warnf("Read git: %s", GIT_PROPERTIES);
            return null;
        }
    }
}
