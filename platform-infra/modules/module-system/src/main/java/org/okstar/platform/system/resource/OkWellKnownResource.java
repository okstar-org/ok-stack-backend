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

package org.okstar.platform.system.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.okstar.platform.system.settings.domain.SysConfIntegration;
import org.okstar.platform.system.settings.service.SysConfIntegrationService;

@Path(".well-known")
public class OkWellKnownResource extends BaseResource {

    @Inject
    SysConfIntegrationService service;

    @GET
    @Path("meet.json")
    public JsonNode meet() {
        ObjectNode node = objectMapper.createObjectNode();

        SysConfIntegration im = service.find();
        node.put("xmppHost", im.getIm().getHost());

        return node;
    }
}
