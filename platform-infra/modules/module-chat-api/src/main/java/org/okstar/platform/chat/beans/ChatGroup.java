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
 *<b> Group </b>
 * <p>
 *  组织和社群，可以是工作、兴趣、社交等方面的群组
 * </p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroup {
    /**
     *  <name>a</name>
     *       <description>
     *       </description>
     *       <shared>false</shared>
     */
    /**
     * 群组名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;

    /**
     * 成员数量
     */
    private int members;

}
