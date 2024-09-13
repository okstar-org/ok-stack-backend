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

package org.okstar.platform.chat.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.igniterealtime.restclient.entity.RosterItemEntity;
import org.igniterealtime.restclient.entity.UserEntities;
import org.okstar.platform.chat.ChatUtils;
import org.okstar.platform.chat.beans.ChatGeneral;
import org.okstar.platform.chat.beans.ChatRosterItem;
import org.okstar.platform.chat.beans.ChatUser;
import org.okstar.platform.chat.openfire.XmppClient;
import org.okstar.platform.common.core.web.bean.Res;

import java.util.List;
import java.util.stream.Collectors;

@Path("user")
public class ChatUserResource {

    @Inject
    XmppClient xmppClient;

    @GET
    @Path("findAll")
    public Res<List<ChatUser>> findAll() {
        UserEntities users = xmppClient.users();
        return Res.ok(ChatUtils.convertUsers(users));
    }

    @GET
    @Path("findByUsername/{username}")
    public Res<ChatUser> findByUsername(@PathParam("username") String username) {
        var user = xmppClient.findUserByUsername(username);
        return Res.ok(ChatUtils.convertUser(user));
    }


    /**
     *
     * @param username@host
     * @return
     */
    @GET
    @Path("findGeneralByUsername/{username}")
    public Res<ChatGeneral> findGeneralByUsername(@PathParam("username") String username) {
        var user = xmppClient.findChatGeneralByUsername(username);
        return Res.ok(user);
    }

    @GET
    @Path("findRosterByUsername/{username}")
    public Res<List<ChatRosterItem>> findRosterByUsername(@PathParam("username") String username) {
        var roster = xmppClient.findRosterByUsername(username);
        List<RosterItemEntity> entities = roster.getRosterItem();
        if (entities == null) {
            return Res.ok();
        }

        List<ChatRosterItem> items = entities.stream().map(e -> ChatRosterItem.builder()
                .jid(e.getJid())
                .nickname(e.getNickname())
                .groups(e.getGroups())
                .subscriptionType(e.getSubscriptionType())
                .build()).collect(Collectors.toList());

        return Res.ok(items);
    }
}
