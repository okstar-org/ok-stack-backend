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

package org.okstar.platform.chat.beans;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    private String naturalName;
    private String roomName;
    private String subject;
    private String description;

    //"owners":["okstar@meet.chuanshaninfo.com"]
    private List<String> owners;
    //最大成员数量
    private int maxUsers;
    //成员数量
    private int members;
    //是否公开
    private boolean publicRoom;

    //持久化房间（保存到数据库）
    private boolean persistent;
    private String password;
    private boolean canChangeNickname;
    private boolean canOccupantsChangeSubject;
    private boolean canOccupantsInvite;
    private boolean logEnabled;

    private Date creationDate;
    private Date modificationDate;
}
