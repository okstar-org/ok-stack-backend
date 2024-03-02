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

package org.okstar.platform.common.core.exception.user;

import org.okstar.platform.common.core.exception.OkRuntimeException;

/**
 * 用户信息异常类
 * 
 *
 */
public class OkUserException extends OkRuntimeException
{
    public OkUserException() {
    }

    public OkUserException(String message) {
        super(message);
    }

    public OkUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public OkUserException(Throwable cause) {
        super(cause);
    }

    public OkUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
