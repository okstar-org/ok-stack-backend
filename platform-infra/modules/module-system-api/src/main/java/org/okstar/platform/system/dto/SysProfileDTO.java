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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.okstar.platform.common.string.OkNameUtil;
import org.okstar.platform.core.user.UserDefines;

import java.util.Date;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysProfileDTO {

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
    /**
     * 语言,格式:zh-CN, zh-TW, zh-HK, en-US
     * @link https://www.loc.gov/standards/iso639-2/php/code_list.php
     */
    private String language;


    public String getFirstName() {
        return Optional.ofNullable(firstName).orElse("");
    }

    public String getLastName() {
        return Optional.ofNullable(lastName).orElse("");
    }

    /**
     * 个人真实名称
     */
    public String getPersonalName() {
        return OkNameUtil.combinePeopleName(language,   getFirstName(),  getLastName());
    }


}
