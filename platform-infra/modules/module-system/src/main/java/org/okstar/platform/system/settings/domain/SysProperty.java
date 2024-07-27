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

package org.okstar.platform.system.settings.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.okstar.platform.system.domain.BaseEntity;


@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"grouping", "domain", "k"}))
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysProperty extends BaseEntity {

    /**
     * 配置组，针对不同类型（实体）配置的区分
     * @see org.okstar.platform.system.settings.SysConfDefines
     */
    @NotBlank
    String grouping;

    /**
     * 配置所在域或范围（比如：隔离不同用户或者其他区域范围）
     */
    String domain;

    //key
    String k;

    //value
    String v;

}
