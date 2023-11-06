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

package org.okstar.platform.system.account.vo;

import lombok.Data;
import org.okstar.platform.common.core.defined.SystemDefines;
import org.okstar.platform.common.core.web.bean.DTO;

@Data
public class SignInAttached extends DTO {

    /** 登录IP */
    private String ip;

    /** 登录地址 */
    private String location;

    /** 浏览器类型 */
    private String browser;

    private String browserVersion;

    /** 操作系统 */
    private String os;

    private String osVersion;

    /** 终端 */
    private SystemDefines.Endpoint endpoint;
}
