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

/**
 * 成员
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatParticipant {
    //成员标识
    private String jid;
    /**
     * 角色
     * <ol>
     * <li>Moderator 主持人</li>
     * <li>None 无</li>
     * <li>Participant 成员</li>
     * <li>Visitor 访客</li>
     * </ol>
     * 更多请参考： @link <a href="https://xmpp.org/extensions/xep-0045.html#roles">Roles</a>
     */
    private String role;

    /**
     * 所属关系
     * <ol>
     *     <li>Owner 创建人</li>
     *     <li>Admin 管理员</li>
     *     <li>Member 成员</li>
     *     <li>Outcast 被拒人（被禁止以后无法再次进入房间）</li>
     *     <li>None (the absence of an affiliation)</li>
     * </ol>
     * 更多请参考： @link <a href="https://xmpp.org/extensions/xep-0045.html#affil">Affiliations</a>
     */
    private String affiliation;
}
