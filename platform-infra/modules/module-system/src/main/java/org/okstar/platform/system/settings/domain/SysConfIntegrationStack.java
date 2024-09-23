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
import org.okstar.platform.system.dto.SysConfItem;
import org.okstar.platform.system.dto.SysPropertyDTO;
import org.okstar.platform.system.settings.SysConfDefines;

import java.util.Map;


/**
 * 系统管理-集成设置-Stack设置
 */
@Data
public class SysConfIntegrationStack implements SysConfItem {
    Map<String, SysPropertyDTO> properties = Maps.newHashMap();

    String fqdn;

    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_INTEGRATION_PREFIX + ".stack";
    }

    @Override
    public void addProperty(SysPropertyDTO property) {
        properties.put(property.getK(), property);

        if (OkStringUtil.equals(property.getK(), "fqdn")) {
            this.fqdn = property.getV();
        }
    }

}
