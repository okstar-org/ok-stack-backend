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
 * 系统管理-基础设置-个人设置
 */
@Data
public class SysConfPersonal implements SysConfItem {

    //语言，格式：zh_CN
    private String language;

    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_PERSONAL_PREFIX;
    }

    @Override
    public void addProperty(SysPropertyDTO property) {
        if (OkStringUtil.equals(property.getK(), "language")) {
            this.language = property.getV();
        }
    }

    public List<SysPropertyDTO> getProperties() {
        ArrayList<SysPropertyDTO> list = new ArrayList<>();

        SysPropertyDTO p = new SysPropertyDTO();
        p.setGrouping(getGroup());
        p.setK("language");
        p.setV(language);
        list.add(p);

        return list;
    }
}