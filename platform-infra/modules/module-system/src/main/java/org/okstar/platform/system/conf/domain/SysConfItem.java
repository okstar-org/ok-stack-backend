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

package org.okstar.platform.system.conf.domain;

import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.List;

/**
 * 集成配置接口
 */
public interface SysConfItem {
    /**
     * 配置组
     */
    String getGroup();

    void addProperty(SysPropertyDTO property);

    default void addProperties(List<SysPropertyDTO> properties) {
        properties.forEach(this::addProperty);
    }

    List<SysPropertyDTO> getProperties();
}
