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

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import org.okstar.platform.system.account.vo.SignInAttached;
import org.okstar.platform.system.domain.BaseEntity;

/**
 * 当前在线会话
 * 
 *
 */
@Data
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"sessionId"})})
public class SysAccountOnline extends BaseEntity
{

    /** 会话编号 */
    private String sessionId;

    /** 帐号 */
    private Long  accountId;

    /** 登录时间 */
    private Long signInTime;

    /*** 登录信息 */
    @Embedded
    private SignInAttached signInAttached;

}
