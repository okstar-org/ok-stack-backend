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

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.RosterEntities;
import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;
import org.okstar.platform.chat.beans.ChatGeneral;
import org.okstar.platform.chat.beans.ChatGroup;
import org.okstar.platform.chat.beans.ChatParticipant;
import org.okstar.platform.chat.beans.ChatRoom;

import java.util.List;

public interface XmppClient {

    RestApiClient makeXmppClient(String host, int xmppAdminPort, String secretKey);

    UserEntities users();

    UserEntity findUserByUsername(String username);

    RosterEntities findRosterByUsername(String username);

    ChatGeneral findChatGeneralByUsername(String username);

    List<ChatRoom> listRooms();

    ChatRoom findRoomByName(String username);

    List<ChatParticipant> findParticipantsByName(String username);

    boolean updateRoom(ChatRoom room);

    List<ChatGroup> listGroups();

    boolean updateGroup(ChatGroup room);
}
