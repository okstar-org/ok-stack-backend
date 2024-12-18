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

package org.okstar.platform.org.connect.exception;

import lombok.Getter;
import org.okstar.platform.org.connect.ConnectorDefines;

/**
 * 连接异常
 */
@Getter
public class ConnectorException extends Exception {

    private final ConnectorDefines.Type type;
    private final String url;

    public ConnectorException(ConnectorDefines.Type type, String url, String message) {
        super(message);
        this.type = type;
        this.url = url;
    }

    public ConnectorException(ConnectorDefines.Type type, String url, Throwable cause) {
        super(cause);
        this.type = type;
        this.url = url;
    }

    public ConnectorException(ConnectorDefines.Type type, String url, String message, Throwable cause) {
        super(message, cause);
        this.type = type;
        this.url = url;
    }


}
