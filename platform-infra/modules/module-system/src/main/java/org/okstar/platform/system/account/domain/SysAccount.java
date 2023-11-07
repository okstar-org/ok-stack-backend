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

package org.okstar.platform.system.account.domain;

import lombok.Data;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.system.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 帐号实体
 */
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class SysAccount extends BaseEntity {

    /**
     * 用户账号
     */
    private String username;

    private String nickname;

    /**
     * 性
     */
    private String firstName;

    /**
     * 名
     */
    private String lastName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 帐号状态
     */
    private AccountDefines.Status status;



    /**
     * 所在国家代号
     * @link https://www.iso.org/obp/ui/#search
     */
    private String iso;
}
