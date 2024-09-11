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

package org.okstar.platform.org.sync.connect.connector.common;


import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.org.sync.connect.SysConEnums;

public class SysConnectorException extends OkRuntimeException {
    private SysConEnums.SysConType type;

    public SysConnectorException(SysConEnums.SysConType type, String message) {
        super(String.format("接口:%s异常，message:%s！", type.getText(), message));
        this.type = type;
    }
}
