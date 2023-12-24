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

package org.okstar.platform.chat.resource;

import jakarta.inject.Inject;
import org.okstar.platform.chat.beans.ChatGroup;
import org.okstar.platform.chat.openfire.OpenfireManager;
import org.okstar.platform.common.core.web.bean.Res;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import java.util.List;

@Path("group")
public class ChatGroupResource {

    @Inject
    OpenfireManager openfireManager;

    @GET
    @Path("findAll")
    public Res<List<ChatGroup>> findAll() {
        List<ChatGroup> rooms = openfireManager.listGroups();
        return Res.ok(rooms);
    }
}
