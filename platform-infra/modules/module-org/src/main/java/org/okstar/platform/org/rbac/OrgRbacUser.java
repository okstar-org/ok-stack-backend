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

package org.okstar.platform.org.rbac;

import lombok.Data;
import org.okstar.platform.org.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

/**
 * RBAC-User
 */
@Data
@Entity
@MappedSuperclass
public class OrgRbacUser extends BaseEntity {
    /**
     * 绑定的帐号
     */
    private Long accountId;

}
