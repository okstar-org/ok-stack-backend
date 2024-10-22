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
import lombok.Data;
import org.okstar.platform.system.conf.SysConfDefines;
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.ArrayList;
import java.util.List;


/**
 * 系统管理-基础设置-个人设置
 */
@Data
public class SysConfWebsite implements SysConfItem {

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
    public List<SysPropertyDTO> getProperties() {
        List<SysPropertyDTO> list = new ArrayList<>();

        SysPropertyDTO l = new SysPropertyDTO();
        l.setGrouping(getGroup());
        l.setK("license");
        l.setV(license);
        list.add(l);

        SysPropertyDTO c = new SysPropertyDTO();
        c.setGrouping(getGroup());
        c.setK("copyright");
        c.setV(copyright);
        list.add(c);

        SysPropertyDTO t = new SysPropertyDTO();
        t.setGrouping(getGroup());
        t.setK("title");
        t.setV(title);
        list.add(t);

        return list;
    }
}
