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
import org.okstar.platform.common.core.defined.UserDefines;
import org.okstar.platform.system.domain.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"accountId"}),
        @UniqueConstraint(columnNames = "identify")})
public class SysProfile extends BaseEntity {
    /**
     * 帐号
     */
    private Long accountId;

    /**
     * 性
     */
    private String firstName;

    /**
     * 名
     */
    private String lastName;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private UserDefines.Gender gender;

    /**
     * 身份证ID
     */
    private String identify;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 电话
     */
    private String telephone;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 地址
     */
    private String address;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 主页地址
     */
    private String website;
}
