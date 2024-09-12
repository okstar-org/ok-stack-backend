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

package org.okstar.platform.org.sync.connect.connector.feishu.proto;


import lombok.Data;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.sync.connect.connector.common.SysConnectorRes;

import java.util.Objects;

@Data
public abstract class FSRes<T> implements SysConnectorRes<T> {
    Integer code;
    String msg;

    @Override
    public boolean success() {
        return Objects.equals(code, 0);
    }

    @Override
    public ConnectorDefines.Type getType() {
        return ConnectorDefines.Type.FS;
    }
}
