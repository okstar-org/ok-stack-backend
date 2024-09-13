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

package org.okstar.platform.org.connect.web;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.connect.api.AccessToken;
import org.okstar.platform.org.connect.connector.ConnectorFactory;
import org.okstar.platform.org.connect.connector.SysConnector;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.connect.exception.ConnectorException;
import org.okstar.platform.org.connect.service.ConnectorConfigService;
import org.okstar.platform.org.resource.BaseResource;

import java.util.List;

@Path("integration")
public class OrgIntegrationResource extends BaseResource {

    @Inject
    ConnectorConfigService connectorConfigService;
    @Inject
    private ConnectorFactory connectorFactory;

    @GET
    @Path("conf/list")
    public Res<List<OrgIntegrateConf>> list() {
        List<OrgIntegrateConf> all = connectorConfigService.findAll();
        return Res.ok(all);
    }

    @POST
    @Path("conf/test/{type}")
    public Res<Boolean> test(@PathParam("type") ConnectorDefines.Type type, OrgIntegrateConf conf) {
        try {
            Log.infof("test for %s", type);
            SysConnector connect = connectorFactory.createConnector(conf);
            AccessToken token = connect.fetchAccessToken();
            return Res.ok(token != null);
        } catch (ConnectorException e) {
            return Res.ok(false);
        }
    }
}
