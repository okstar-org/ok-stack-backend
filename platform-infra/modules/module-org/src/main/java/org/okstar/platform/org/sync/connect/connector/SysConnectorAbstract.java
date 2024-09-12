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

package org.okstar.platform.org.sync.connect.connector;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.web.OkRestUtil;
import org.okstar.platform.common.web.rest.transport.ClientFactory;
import org.okstar.platform.common.web.rest.transport.RestClient;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.sync.connect.SysConnector;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.connect.api.AccessToken;

@Data
@Slf4j
public abstract class SysConnectorAbstract implements SysConnector {

    protected AccessToken accessToken;

    protected OrgIntegrateConf conf;

    @Override
    public ConnectorDefines.Type getType() {
        return conf.getType();
    }

    @Override
    public String getRequestUrl(String url) {
        return String.format("%s/%s", conf.getBaseUrl(), url);
    }

    public RestClient getClient() {
        ClientFactory factory = OkRestUtil.getInstance(conf.getBaseUrl()).getClientFactory();
        return factory.createClient();
    }


}
