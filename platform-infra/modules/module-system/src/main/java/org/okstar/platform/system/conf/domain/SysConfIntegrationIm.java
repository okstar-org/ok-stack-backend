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
 * 系统管理-集成设置-IM设置
 */
@Data
public class SysConfIntegrationIm implements SysConfItem {

    private String host;

    //服务端口(默认5222)
    private int port = 5222;

    //管理端口
    private int adminPort;
    private String apiSecret;

    @Override
    public String getGroup() {
        return SysConfDefines.CONF_GROUP_INTEGRATION_PREFIX + ".im";
    }

    @Override
    public void addProperty(SysPropertyDTO property) {
//        properties.put(property.getK(), property);

        if (OkStringUtil.equalsIgnoreCase(property.getK(), "host")) {
            setHost(property.getV());
        } else if (OkStringUtil.equalsIgnoreCase(property.getK(), "admin-port")) {
            setAdminPort(Integer.parseInt(property.getV()));
        } else if (OkStringUtil.equalsIgnoreCase(property.getK(), "port")) {
            setPort(Integer.parseInt(property.getV()));
        } else if (OkStringUtil.equalsIgnoreCase(property.getK(), "api-secret")) {
            setApiSecret(property.getV());
        }

    }

    @Override
    public List<SysPropertyDTO> getProperties(){

        List<SysPropertyDTO> list = new ArrayList<>();

        //host
        SysPropertyDTO pHost = new SysPropertyDTO();
        pHost.setK("host");
        pHost.setV(getHost());
        pHost.setGrouping(getGroup());
        list.add(pHost);

        //admin-port
        SysPropertyDTO pAdminPort = new SysPropertyDTO();
        pAdminPort.setK("admin-port");
        pAdminPort.setV(String.valueOf(getAdminPort()));
        pAdminPort.setGrouping(getGroup());
        list.add(pAdminPort);

        //port
        SysPropertyDTO pPort = new SysPropertyDTO();
        pPort.setK("port");
        pPort.setV(String.valueOf(getPort()));
        pPort.setGrouping(getGroup());
        list.add(pPort);

        SysPropertyDTO pApiSecret = new SysPropertyDTO();
        pApiSecret.setK("api-secret");
        pApiSecret.setV(String.valueOf(getApiSecret()));
        pApiSecret.setGrouping(getGroup());
        list.add(pApiSecret);

        return list;
    }


}
