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

package org.okstar.platform.system.settings.domain;

import com.google.common.collect.Maps;
import lombok.Data;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.settings.SysConfDefines;

import java.util.Map;


/**
 * 系统管理-基础设置-个人设置
 */
@Data
public class SysConfPersonal implements SysConfItem {
    Map<String, SysProperty> properties = Maps.newHashMap();

    //语言，格式：zh_CN
    String language;


    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_PERSONAL_PREFIX;
    }

    @Override
    public void addProperty(SysProperty property) {
        properties.put(property.getK(), property);
        if (OkStringUtil.equals(property.getK(), "language")) {
            this.language = property.getV();
        }
    }
}
