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

package org.okstar.platform.org.sync.connect;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.connect.exception.ConnectorException;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.proto.SysConnAccessToken;
import org.okstar.platform.org.connect.api.Department;

import java.util.List;

@QuarkusTest
class ConnectorFactoryTest {

    @Inject
    ConnectorFactory connectorFactory;

    @Test
    void getConnectorDD() throws ConnectorException {

        OrgIntegrateConf conf = new OrgIntegrateConf();
        conf.setType(ConnectorDefines.Type.DD);
        conf.setAppId("1252072514");
        conf.setCertKey("dingwrdztyvmdk4kpsmk");
        conf.setCertSecret("Fw-Xq3jhYkNxqmSM_-TIkK_JEEucjKtfkprXGL2Zh-xSh2Dzz7vxhF5f8LlQDrNe");

        SysConnector connector = connectorFactory.getConnector(conf);
        Assert.assertNotNull(connector);


        SysConnAccessToken accessToken = connector.fetchAccessToken();
        Log.infof("accessToken: %s", accessToken.getAccessToken());
        Assert.assertTrue(OkStringUtil.isNotEmpty(accessToken.getAccessToken()));

        List<Department> list = connector.getDepartmentList("1");
        Assert.assertNotNull(list);
        list.forEach(e->{
            Log.infof("dept:%s", e.getName());
        });

    }
}