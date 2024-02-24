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

package org.okstar.platform.chat;

import org.igniterealtime.restclient.entity.*;
import org.okstar.platform.chat.beans.ChatGroup;
import org.okstar.platform.chat.beans.ChatParticipant;
import org.okstar.platform.chat.beans.ChatRoom;
import org.okstar.platform.chat.beans.ChatUser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {
    private ChatUtils() {
    }

    public static ChatUser convertUser(UserEntity userEntity) {
        ChatUser user = new ChatUser();
        user.setUsername(userEntity.getUsername());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        user.setOnline(userEntity.isOnline());
        return user;
    }

    public static List<ChatUser> convertUsers(UserEntities userEntities) {
        if (userEntities.getUsers() == null || userEntities.getUsers().isEmpty()) {
            return Collections.emptyList();
        }
        return userEntities.getUsers().stream().map(ChatUtils::convertUser).collect(Collectors.toList());
    }

    public static ChatRoom convertRoom(MUCRoomEntity room) {
        if (room == null)
            return null;
        return ChatRoom.builder()
                .roomName(room.getRoomName())
                .subject(room.getSubject())
                .naturalName(room.getNaturalName())
                .description(room.getDescription())
                .owners(room.getOwners())
                .maxUsers(room.getMaxUsers())
                .members(room.getMembers() == null ? 0 : room.getMembers().size())
                .password(room.getPassword())
                .publicRoom(room.isPublicRoom())
                .persistent(room.isPersistent())
                .canChangeNickname(room.isCanChangeNickname())
                .canOccupantsChangeSubject(room.isCanOccupantsChangeSubject())
                .canOccupantsInvite(room.isCanOccupantsInvite())
                .logEnabled(room.isLogEnabled())
                .creationDate(room.getCreationDate())
                .modificationDate(room.getModificationDate())
                .build();
    }

    public static MUCRoomEntity convertRoom(ChatRoom room) {
        MUCRoomEntity entity = new MUCRoomEntity();
        entity.setRoomName(room.getRoomName());
        entity.setNaturalName(room.getNaturalName());
        entity.setSubject(room.getSubject());
        entity.setDescription(room.getDescription());
        entity.setPassword(room.getPassword());
        entity.setCanChangeNickname(room.isCanChangeNickname());
        entity.setCanOccupantsChangeSubject(room.isCanOccupantsChangeSubject());
        entity.setCanOccupantsInvite(room.isCanOccupantsInvite());
        entity.setLogEnabled(room.isLogEnabled());
        return entity;
    }

    public static ChatParticipant convertParticipant(ParticipantEntity participant) {
        return ChatParticipant.builder()
                .jid(participant.getJid())
                .role(participant.getRole())
                .affiliation(participant.getAffiliation())
                .build();
    }

    public static ChatGroup convertGroup(GroupEntity e) {
        return ChatGroup.builder()
                .name(e.getName())
                .description(e.getDescription())
                .members(e.getMembers()==null ? 0 : e.getMembers().size())
                .build();
    }

    public static GroupEntity convertGroup(ChatGroup room) {
        GroupEntity entity = new GroupEntity();
        entity.setName(room.getName());
        entity.setDescription(room.getDescription());
        return entity;
    }
}
