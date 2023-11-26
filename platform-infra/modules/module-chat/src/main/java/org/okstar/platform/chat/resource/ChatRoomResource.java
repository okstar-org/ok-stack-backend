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

import org.okstar.platform.chat.beans.ChatParticipant;
import org.okstar.platform.chat.beans.ChatRoom;
import org.okstar.platform.chat.openfire.OpenfireManager;
import org.okstar.platform.common.core.web.bean.Res;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("room")
public class ChatRoomResource {

    @Inject
    OpenfireManager openfireManager;

    @GET
    @Path("findAll")
    public Res<List<ChatRoom>> findAll() {
        List<ChatRoom> rooms = openfireManager.listRooms();
        return Res.ok(rooms);
    }

    @GET
    @Path("findByName/{username}")
    public Res<ChatRoom> findByName(@PathParam("username") String username) {
        ChatRoom room = openfireManager.findRoomByName(username);
        return Res.ok(room);
    }

    @GET
    @Path("findParticipantsByName/{username}")
    public Res<List<ChatParticipant>> findParticipantsByName(@PathParam("username") String username) {
        List<ChatParticipant> list = openfireManager.findParticipantsByName(username);
        return Res.ok(list);
    }
}
