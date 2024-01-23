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

package org.okstar.platform.chat.openfire;

import io.quarkus.arc.Arc;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.Getter;
import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.*;
import org.igniterealtime.restclient.enums.SupportedMediaType;
import org.okstar.platform.chat.ChatUtils;
import org.okstar.platform.chat.beans.ChatGeneral;
import org.okstar.platform.chat.beans.ChatGroup;
import org.okstar.platform.chat.beans.ChatParticipant;
import org.okstar.platform.chat.beans.ChatRoom;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Getter
@ApplicationScoped
public class OpenfireManager extends Thread {
    //    @ConfigProperty(name = "chat.auth.secret-key")
    private String authSecretKey = "JxHQUSsqDLJbArL7";

    private RestApiClient restApiClient;


    public void init(@Observes StartupEvent e) {
        setDaemon(true);
        setName("OpenfireManager");

        // TODO 固定地址
        AuthenticationToken token = new AuthenticationToken(authSecretKey);
        restApiClient = new RestApiClient(
                "http://meet.chuanshaninfo.com",
                9090, //
                token, SupportedMediaType.JSON);

        ExecutorService executorService = Arc.container().getExecutorService();
        executorService.submit(this);
    }

    @Override
    public void run() {


    }

    public UserEntities users() {
        UserEntities users = restApiClient.getUsers();
        Log.infof("users:%s", users);
        return users;
    }

    public UserEntity findUserByUsername(String username) {
        Log.infof("findUserByUsername:%s", username);
        UserEntity user = restApiClient.getUser(username);
        Log.infof("user:%s", user);
        return user;
    }

    public RosterEntities findRosterByUsername(String username) {
        Log.infof("findRosterByUsername:%s", username);
        RosterEntities rosterEntities = restApiClient.getRoster(username);
        Log.infof("rosterEntities:%s", rosterEntities.getRosterItem());
        return rosterEntities;
    }

    public ChatGeneral findChatGeneralByUsername(String username) {

        RosterEntities roster = restApiClient.getRoster(username);
        UserGroupsEntity groups = restApiClient.getUserGroups(username);
        MessageArchiveEntities archive = restApiClient.getMessageArchive(username + "@meet.chuanshaninfo.com");

        return ChatGeneral.builder()//
                .groups(groups.getGroupNames() == null ? 0 : groups.getGroupNames().size())
                .contacts(roster.getRosterItem() == null ? 0 : roster.getRosterItem().size())
                .msgs(archive.getArchive() == null ? 0 : archive.getArchive().getCount())
                .build();
    }


    public List<ChatRoom> listRooms() {
        Map<String, String> q = new HashMap<>();
        q.put("type", "all");
        var rooms = restApiClient.getChatRooms(q);
        return rooms.getChatRooms().stream()        //
                .map(r -> ChatUtils.convertRoom(r)) //
                .collect(Collectors.toList());
    }

    public ChatRoom findRoomByName(String username) {
        MUCRoomEntity room = restApiClient.getChatRoom(username);
        return ChatUtils.convertRoom(room);
    }

    public List<ChatParticipant> findParticipantsByName(String username) {
        ParticipantEntities participants = restApiClient.getChatRoomParticipants(username);
        if (participants == null || participants.getParticipants() == null)
            return Collections.emptyList();
        return participants.getParticipants().stream().map(e ->
                ChatUtils.convertParticipant(e)
        ).toList();
    }

    public boolean updateRoom(ChatRoom room) {
        MUCRoomEntity entity = ChatUtils.convertRoom(room);
        var response = restApiClient.updateChatRoom(entity);
        return response.getStatus() == 200;
    }

    public List<ChatGroup> listGroups() {
        var groups = restApiClient.getGroups();

        List<GroupEntity> list = groups.getGroups();
        if (list == null)
            return Collections.emptyList();

        return list.stream().map(ChatUtils::convertGroup).toList();
    }


    public boolean updateGroup(ChatGroup room) {
        GroupEntity entity = ChatUtils.convertGroup(room);
        var response = restApiClient.updateGroup(entity);
        return response.getStatus() == 200;
    }

}
