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
import org.okstar.platform.common.core.web.bean.DTO;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRosterItem extends DTO {

    private String jid;

    private String nickname;

    /**
     * SubType
     * * Indicates the roster item should be removed.
     * REMOVE(-1),
     * No subscription is established.
     * NONE(0),
     * * The roster owner has a subscription to the roster item's presence.
     * TO(1),
     * * The roster item has a subscription to the roster owner's presence.
     * FROM(2),
     * * The roster item and owner have a mutual subscription.
     * BOTH(3);
     */
    private int subscriptionType;

    private List<String> groups;
}
