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

import lombok.Data;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.conf.SysConfDefines;
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统管理-集成设置-Stack设置
 */
@Data
public class SysConfIntegrationStack implements SysConfItem {

    private String fqdn;

    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_INTEGRATION_PREFIX + ".stack";
    }

    @Override
    public void addProperty(SysPropertyDTO property) {
        if (OkStringUtil.equals(property.getK(), "fqdn")) {
            this.fqdn = property.getV();
        }
    }

    @Override
    public List<SysPropertyDTO> getProperties() {
        ArrayList<SysPropertyDTO> list = new ArrayList<>();

        SysPropertyDTO dto = new SysPropertyDTO();
        dto.setK("fqdn");
        dto.setV(fqdn);
        dto.setGrouping(getGroup());
        list.add(dto);

        return list;
    }

}
