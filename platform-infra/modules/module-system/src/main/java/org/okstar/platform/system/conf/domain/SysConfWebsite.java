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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import lombok.Data;
import org.okstar.platform.system.conf.SysConfDefines;
import org.okstar.platform.system.dto.SysConfItem;
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.Map;


/**
 * 系统管理-基础设置-个人设置
 */
@Data
public class SysConfWebsite implements SysConfItem {

    private Map<String, SysPropertyDTO> properties = Maps.newHashMap();

    private String title;
    //备案号
    private String license;
    //版权所有
    private String copyright;

    @JsonIgnore
    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_SETTINGS_PREFIX;
    }

    @Override
    public void addProperty(SysPropertyDTO property) {
        properties.put(property.getK(), property);
        switch (property.getK()) {
            case "license":
                this.license = property.getV();
                break;
            case "copyright":
                this.copyright = property.getV();
                break;
            case "title":
                this.title = property.getV();
                break;
        }
    }

    @JsonIgnore
    public Map<String, SysPropertyDTO> getProperties() {
        if (license != null) {
            SysPropertyDTO p = new SysPropertyDTO();
            p.setGrouping(getGroup());
            p.setK("license");
            p.setV(license);
            properties.put(p.getK(), p);
        }

        if (license != null) {
            SysPropertyDTO p = new SysPropertyDTO();
            p.setGrouping(getGroup());
            p.setK("copyright");
            p.setV(copyright);
            properties.put(p.getK(), p);
        }

        if (title != null) {
            SysPropertyDTO p = new SysPropertyDTO();
            p.setGrouping(getGroup());
            p.setK("title");
            p.setV(title);
            properties.put(p.getK(), p);
        }


        return properties;
    }
}
