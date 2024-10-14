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

package org.okstar.platform.system.dto;

import lombok.*;
import org.okstar.platform.common.web.bean.DTO;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysPropertyDTO extends DTO {

    private String grouping;

    /**
     * 配置所在域或范围（比如：隔离不同用户或者其他区域范围）
     */
    private String domain;

    //key
    private String k;

    //value
    private String v;
}
