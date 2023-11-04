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

package org.okstar.platform.org.account;

import lombok.Data;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.org.account.SysAccount;
import org.okstar.platform.org.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 用户绑定

 */
@Data
@Entity
public class SysAccountBind extends BaseEntity {

    /**
     * 绑定到的帐号
     */
    @ManyToOne
    SysAccount account;

    /**
     * 绑定类型
     */
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
