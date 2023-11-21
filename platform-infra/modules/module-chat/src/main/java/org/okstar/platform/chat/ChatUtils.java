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

import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;
import org.okstar.platform.chat.beans.ChatUser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChatUtils {
    private ChatUtils() {
    }

    public static ChatUser entity2bean(UserEntity userEntity) {
        ChatUser user = new ChatUser();
        user.setUsername(userEntity.getUsername());
        user.setName(userEntity.getName());
        user.setEmail(userEntity.getEmail());
        return user;
    }

    public static List<ChatUser> entity2beans(UserEntities userEntities) {
        if (userEntities.getUsers() == null || userEntities.getUsers().isEmpty()) {
            return Collections.emptyList();
        }
        return userEntities.getUsers().stream().map(ChatUtils::entity2bean).collect(Collectors.toList());
    }

}
