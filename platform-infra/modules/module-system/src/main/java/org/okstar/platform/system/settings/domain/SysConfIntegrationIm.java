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
 * 系统管理-集成设置-IM设置
 */
@Data
public class SysConfIntegrationIm implements SysConfItem {
    Map<String, SysProperty> properties = Maps.newHashMap();

    String host;
    String apiSecret;
    int adminPort;

    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_INTEGRATION_PREFIX + ".im";
    }

    @Override
    public void addProperty(SysProperty property) {
        properties.put(property.getK(), property);

        if (OkStringUtil.equalsIgnoreCase(property.getK(), "host")) {
            setHost(property.getV());
        } else if (OkStringUtil.equalsIgnoreCase(property.getK(), "admin-port")) {
            setAdminPort(Integer.parseInt(property.getV()));
        } else if (OkStringUtil.equalsIgnoreCase(property.getK(), "api-secret")) {
            setApiSecret(property.getV());
        }

    }

}
