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

package org.okstar.platform.chat.beans;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * <b>XMPP Room 多人聊天室</b>
 * <p>
 * 是基于XMPP协议实现的多用户聊天环境,
 * 主要用于即时消息传递和在线状态管理，
 * 适用于需要实时交流和协作的场景；
 * </p>
 */
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
    //密码
    private String password;
    //创建时间
    private Date creationDate;
    //修改时间
    private Date modificationDate;
    //日志开启
    private boolean logEnabled;

    //是否可修名称
    private boolean canChangeNickname;
    //是否可修改主题
    private boolean canOccupantsChangeSubject;
    //成员是否可邀请他人
    private boolean canOccupantsInvite;
}
