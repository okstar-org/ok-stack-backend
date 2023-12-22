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

import jakarta.persistence.Entity;
import lombok.Data;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.datasource.domain.OkEntity;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * 用户绑定

 */
@Data
@Entity
public class SysAccountBind extends OkEntity {

    /**
     * 绑定到的帐号
     */
//    @ManyToOne
    Long accountId;

    /**
     * 绑定类型
     */
    @Enumerated(EnumType.STRING)
    AccountDefines.BindType bindType;

    /**
     * 绑定值
     */
    String bindValue;

    /**
     * 是否合法(验证过)
     */
    Boolean isValid;
}
