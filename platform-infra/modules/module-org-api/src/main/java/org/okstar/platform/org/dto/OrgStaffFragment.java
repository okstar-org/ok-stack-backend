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

package org.okstar.platform.org.dto;

import jakarta.persistence.Embeddable;
import lombok.Data;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.core.user.UserDefines;

import java.util.Date;

/**
 * 人员片段
 */
@Data
@Embeddable
public class OrgStaffFragment {
    /**
     * 编号
     */
    private String no;

    /**
     * 性
     */
    private String firstName;

    /**
     * 名
     */
    private String lastName;

    /**
     * 名称
     */
    private String name;

    public String getName() {
        if(OkStringUtil.isNotEmpty(name))
            return name;
        return OkStringUtil.combinePeopleName(language, firstName, lastName);
    }

    /**
     * 性别
     */
    private UserDefines.Gender gender;

    /**
     * 身份证ID
     */
    private String identity;

    /**
     * ISO国家代号
     * @link https://www.iso.org/iso-3166-country-codes.html
     * @link https://www.iso.org/obp/ui/#search/code/
     */
    private String iso;

    /**
     * 语言,格式:zh-CN, zh-TW, zh-HK, en-US
     * @link https://www.loc.gov/standards/iso639-2/php/code_list.php
     */
    private String language;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 备注
     */
    private String descr;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 居住国家
     */
    private String country;

    /**
     * 居住城市
     */
    private String city;

    /**
     * 居住地址
     */
    private String livingIn;
}
