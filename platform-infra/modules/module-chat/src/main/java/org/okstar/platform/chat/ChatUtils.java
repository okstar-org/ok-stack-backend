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

package org.okstar.platform.chat;

import org.igniterealtime.restclient.entity.*;
import org.okstar.platform.chat.beans.ChatGroup;
import org.okstar.platform.chat.beans.ChatParticipant;
import org.okstar.platform.chat.beans.ChatRoom;
import org.okstar.platform.chat.beans.ChatUser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p> 聊天管理工具类 </p>
 *
 * <ol>
 *     <li>将`聊天系统/Openfire`相关实体转成`本地系统/Stack`相关实体</li>
 *     <li>或者进行逆向互转。</li>
 * </ol>
 */
public class ChatUtils {

    private ChatUtils() {
    }

    /**
     * 将`聊天用户`转成`本地用户`
     *
     * @param userEntity 聊天用户
     * @return 本地用户
     */
    public static ChatUser convertUser(UserEntity userEntity) {
        ChatUser user = new ChatUser();
        user.setUsername(userEntity.getUsername());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        user.setOnline(userEntity.isOnline());
        return user;
    }

    /**
     * 将`聊天用户`转成`本地用户`
     *
     * @param userEntities 聊天用户
     * @return 本地用户
     */
    public static List<ChatUser> convertUsers(UserEntities userEntities) {
        if (userEntities.getUsers() == null || userEntities.getUsers().isEmpty()) {
            return Collections.emptyList();
        }
        return userEntities.getUsers().stream().map(ChatUtils::convertUser).collect(Collectors.toList());
    }

    /**
     * 将`聊天房间`转成`本地房间`
     *
     * @param room 聊天房间
     * @return 本地房间
     */
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

    /**
     * 将`本地房间`转成`聊天房间`
     *
     * @param room 本地房间
     * @return 聊天房间
     */
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

    /**
     * 将`聊天成员`转成`本地成员`
     *
     * @param participant 聊天成员
     * @return ChatParticipant
     */
    public static ChatParticipant convertParticipant(ParticipantEntity participant) {
        return ChatParticipant.builder()
                .jid(participant.getJid())
                .role(participant.getRole())
                .affiliation(participant.getAffiliation())
                .build();
    }

    /**
     * 将`聊天群`转成`本地群`
     *
     * @param groupEntity 聊天群
     * @return ChatGroup
     */
    public static ChatGroup convertGroup(GroupEntity groupEntity) {
        return ChatGroup.builder()
                .name(groupEntity.getName())
                .description(groupEntity.getDescription())
                .members(groupEntity.getMembers() == null ? 0 : groupEntity.getMembers().size())
                .build();
    }

    /**
     *
     * 将`本地群`转成`聊天群`
     * @param chatGroup 本地群
     * @return 聊天群
     */
    public static GroupEntity convertGroup(ChatGroup chatGroup) {
        GroupEntity entity = new GroupEntity();
        entity.setName(chatGroup.getName());
        entity.setDescription(chatGroup.getDescription());
        return entity;
    }
}
