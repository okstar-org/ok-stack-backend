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

package org.okstar.platform.chat.openfire;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.*;
import org.igniterealtime.restclient.enums.SupportedMediaType;
import org.okstar.platform.chat.ChatUtils;
import org.okstar.platform.chat.beans.ChatGeneral;
import org.okstar.platform.chat.beans.ChatGroup;
import org.okstar.platform.chat.beans.ChatParticipant;
import org.okstar.platform.chat.beans.ChatRoom;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.system.dto.SysSetGlobalDTO;
import org.okstar.platform.system.dto.SysSetXmppDTO;
import org.okstar.platform.system.rpc.SysSettingsRpc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class OpenfireManager implements XmppClient {

    private RestApiClient restApiClient;
    private SysSetXmppDTO xmppConf;

    @Inject
    @RestClient
    SysSettingsRpc settingsRpc;

    public RestApiClient ensure() {
        SysSetGlobalDTO global = settingsRpc.getGlobal();
        if (global == null) {
            Log.warnf("Can not find global settings!");
            return null;
        }

        SysSetXmppDTO xmpp = global.extraXmpp();
        if (OkStringUtil.isEmpty(xmpp.getHost())) {
            Log.warnf("Unable to find xmpp host!");
            return null;
        }

        if (restApiClient != null && xmpp.equals(xmppConf)) {
            return restApiClient;
        }

        int xmppAdminPort = xmpp.getAdminPort();
        if (xmppAdminPort == 0) {
            Log.warnf("Unable to find xmpp admin port!");
            return null;
        }

        String secretKey = xmpp.getApiSecretKey();
        if (OkStringUtil.isEmpty(secretKey)) {
            Log.warnf("Unable to find xmpp api secret key!");
            return null;
        }

        AuthenticationToken token = new AuthenticationToken(secretKey);
        restApiClient = new RestApiClient(
                "http://" + xmpp.getHost(), xmppAdminPort, //
                token, SupportedMediaType.JSON);

        xmppConf = xmpp;
        return restApiClient;
    }

    @Override
    public UserEntities users() {
        UserEntities users = ensure().getUsers();
        Log.infof("users:%s", users);
        return users;
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        Log.infof("findUserByUsername:%s", username);
        UserEntity user = ensure().getUser(username);
        Log.infof("user:%s", user);
        return user;
    }

    @Override
    public RosterEntities findRosterByUsername(String username) {
        Log.infof("findRosterByUsername:%s", username);
        RosterEntities rosterEntities = ensure().getRoster(username);
        Log.infof("rosterEntities:%s", rosterEntities.getRosterItem());
        return rosterEntities;
    }

    @Override
    public ChatGeneral findChatGeneralByUsername(String username) {
        RestApiClient ensure = ensure();
        RosterEntities roster = ensure.getRoster(username);
        UserGroupsEntity groups = ensure.getUserGroups(username);
        MessageArchiveEntities archive = ensure.getMessageArchive(username);

        return ChatGeneral.builder()//
                .groups(groups.getGroupNames() == null ? 0 : groups.getGroupNames().size())
                .contacts(roster.getRosterItem() == null ? 0 : roster.getRosterItem().size())
                .msgs(archive.getArchive() == null ? 0 : archive.getArchive().getCount())
                .build();
    }

    @Override
    public List<ChatRoom> listRooms() {
        Map<String, String> q = new HashMap<>();
        q.put("type", "all");
        var rooms = ensure().getChatRooms(q);
        return rooms.getChatRooms().stream()        //
                .map(r -> ChatUtils.convertRoom(r)) //
                .collect(Collectors.toList());
    }

    @Override
    public ChatRoom findRoomByName(String username) {
        MUCRoomEntity room = ensure().getChatRoom(username);
        return ChatUtils.convertRoom(room);
    }

    @Override
    public List<ChatParticipant> findParticipantsByName(String username) {
        ParticipantEntities participants = ensure().getChatRoomParticipants(username);
        if (participants == null || participants.getParticipants() == null)
            return Collections.emptyList();
        return participants.getParticipants().stream().map(e ->
                ChatUtils.convertParticipant(e)
        ).toList();
    }

    @Override
    public boolean updateRoom(ChatRoom room) {
        MUCRoomEntity entity = ChatUtils.convertRoom(room);
        var response = ensure().updateChatRoom(entity);
        return response.getStatus() == 200;
    }

    @Override
    public List<ChatGroup> listGroups() {
        var groups = ensure().getGroups();
        List<GroupEntity> list = groups.getGroups();
        if (list == null)
            return Collections.emptyList();
        return list.stream().map(ChatUtils::convertGroup).toList();
    }

    @Override
    public boolean updateGroup(ChatGroup room) {
        GroupEntity entity = ChatUtils.convertGroup(room);
        var response = ensure().updateGroup(entity);
        return response.getStatus() == 200;
    }

}
