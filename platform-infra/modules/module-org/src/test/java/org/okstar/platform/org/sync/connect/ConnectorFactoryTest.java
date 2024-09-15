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
import org.okstar.platform.org.connect.api.UserInfo;
import org.okstar.platform.org.connect.connector.ConnectorFactory;
import org.okstar.platform.org.connect.connector.OrgConnector;
import org.okstar.platform.org.connect.exception.ConnectorException;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.connect.api.UserId;
import org.okstar.platform.org.connect.api.AccessToken;
import org.okstar.platform.org.connect.api.Department;
import org.okstar.platform.org.connect.service.ConnectorConfigService;

import java.util.List;

@QuarkusTest
class ConnectorFactoryTest {

    @Inject
    ConnectorFactory connectorFactory;
    @Inject
    ConnectorConfigService connectorConfigService;

    /**
     *  "https://qyapi.weixin.qq.com/cgi-bin";
     */

    @Test
    void saveConfigDD(){
        OrgIntegrateConf conf = new OrgIntegrateConf();
        conf.setType(ConnectorDefines.Type.DD);
        conf.setAppId("1252072514");
        conf.setCertKey("dingwrdztyvmdk4kpsmk");
        conf.setCertSecret("Fw-Xq3jhYkNxqmSM_-TIkK_JEEucjKtfkprXGL2Zh-xSh2Dzz7vxhF5f8LlQDrNe");
        conf.setBaseUrl("https://oapi.dingtalk.com");
        conf.setRootDeptId("1");
        connectorConfigService.save(conf);
    }


    @Test
    void createConnectorDD() throws ConnectorException {

        OrgIntegrateConf conf = connectorConfigService.findOne(ConnectorDefines.Type.DD);
        Assert.assertNotNull(conf);

        createConnector(conf);
    }

    @Test
    void saveConfigFs(){
        OrgIntegrateConf conf = new OrgIntegrateConf();
        conf.setType(ConnectorDefines.Type.FS);
        conf.setAppId("cli_a66859d3b83cd00e");
        conf.setCertSecret("JafMneSQsiitx95dRJ60TbbIOonvHsHg");
        conf.setBaseUrl("https://open.feishu.cn/open-apis");
        conf.setRootDeptId("0");
        connectorConfigService.save(conf);
    }


    @Test
    void createConnectorFs() throws ConnectorException {
        OrgIntegrateConf conf = connectorConfigService.findOne(ConnectorDefines.Type.FS);
        Assert.assertNotNull(conf);
        createConnector(conf);
    }

    @Test
    void saveConfigWx(){
        OrgIntegrateConf conf = new OrgIntegrateConf();
        conf.setType(ConnectorDefines.Type.WX);
        conf.setAppId("1000002");
        conf.setCertKey("wwd2bcf3d12b08bb5d");
        conf.setCertSecret("zES8GX6xFb-aoK5kAYqGL9gCcAYl9jfLu9xaMp7VbQU");
        conf.setBaseUrl("https://qyapi.weixin.qq.com/cgi-bin");
        conf.setRootDeptId("1");
        connectorConfigService.save(conf);
    }

    @Test
    void createConnectorWx() throws ConnectorException {
        OrgIntegrateConf conf = connectorConfigService.findOne(ConnectorDefines.Type.WX);
        Assert.assertNotNull(conf);
        createConnector(conf);
    }

    private void createConnector(OrgIntegrateConf conf) throws ConnectorException {
        OrgConnector connector = connectorFactory.createConnector(conf);
        Assert.assertNotNull(connector);

        AccessToken accessToken = connector.fetchAccessToken();
        Log.infof("accessToken: %s", accessToken.getAccessToken());
        Assert.assertTrue(OkStringUtil.isNotEmpty(accessToken.getAccessToken()));

        listDepartmentUsers(connector, conf.getRootDeptId());
    }

    private static void listDepartmentUsers(OrgConnector connector, String deptId) throws ConnectorException {
        List<Department> list = connector.getDepartmentList(deptId);
        Assert.assertNotNull(list);
        for (Department e : list) {
            Log.infof("dept=> %s", e);
            List<UserId> userIdList = connector.getUserIdList(e);
            for (UserId userId : userIdList) {
                Log.infof("userId=> %s", userId.getUserId());
                UserInfo info = connector.getUserInfo(userId.getUserId());
                Log.infof("user info=> %s", info);
            }
            listDepartmentUsers(connector, e.getId());
        }

    }
}