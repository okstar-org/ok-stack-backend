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
import org.igniterealtime.restclient.entity.RosterItemEntity;
import org.igniterealtime.restclient.entity.UserEntities;
import org.okstar.platform.chat.ChatUtils;
import org.okstar.platform.chat.beans.ChatGeneral;
import org.okstar.platform.chat.beans.ChatRosterItem;
import org.okstar.platform.chat.beans.ChatUser;
import org.okstar.platform.chat.openfire.OpenfireManager;
import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@Path("user")
public class ChatUserResource {

    @Inject
    OpenfireManager openfireManager;

    @GET
    @Path("findAll")
    public Res<List<ChatUser>> findAll() {
        UserEntities users = openfireManager.users();
        return Res.ok(ChatUtils.convertUsers(users));
    }

    @GET
    @Path("findByUsername/{username}")
    public Res<ChatUser> findByUsername(@PathParam("username") String username) {
        var user = openfireManager.findUserByUsername(username);
        return Res.ok(Req.empty(), ChatUtils.convertUser(user));
    }


    @GET
    @Path("findGeneralByUsername/{username}")
    public Res<ChatGeneral> findGeneralByUsername(@PathParam("username") String username) {
        var user = openfireManager.findChatGeneralByUsername(username);
        return Res.ok(Req.empty(), (user));
    }

    @GET
    @Path("findRosterByUsername/{username}")
    public Res<List<ChatRosterItem>> findRosterByUsername(@PathParam("username") String username) {
        var roster = openfireManager.findRosterByUsername(username);
        List<RosterItemEntity> entities = roster.getRosterItem();
        if (entities == null) {
            return Res.ok(Req.empty());
        }

        List<ChatRosterItem> items = entities.stream().map(e -> ChatRosterItem.builder()
                .jid(e.getJid())
                .nickname(e.getNickname())
                .groups(e.getGroups())
                .subscriptionType(e.getSubscriptionType())
                .build()).collect(Collectors.toList());

        return Res.ok(Req.empty(), items);
    }
}
