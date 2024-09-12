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

package org.okstar.platform.org.sync.connect.connector.feishu;


import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.web.rest.transport.RestClient;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.sync.connect.connector.SysConnectorAbstract;
import org.okstar.platform.org.sync.connect.connector.common.OkAssertConnector;
import org.okstar.platform.org.sync.connect.connector.feishu.proto.access.req.FSAccessTokenReq;
import org.okstar.platform.org.sync.connect.connector.feishu.proto.access.res.FSAccessTokenRes;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.dto.SysConUser;
import org.okstar.platform.org.sync.connect.proto.SysConnAccessToken;
import org.okstar.platform.org.connect.api.Department;
import org.okstar.platform.org.sync.connect.proto.SysConnUserInfo;

import java.util.List;

@Slf4j
public class SysConnectorFS extends SysConnectorAbstract {


    public SysConnectorFS(OrgIntegrateConf conf) {
        this.conf = conf;
    }

    @Override
    public ConnectorDefines.Type getType() {
        return ConnectorDefines.Type.FS;
    }

    @Override
    public String getBaseUrl() {
        return "https://open.feishu.cn/open-apis";
    }

    /**
     * 获取access token
     * curl -i -X POST 'https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal' \
     * -H 'content-type:application/json; charset=utf-8' \
     * -d '{
     * "app_id": "",
     * "app_secret": ""
     * }'
     */

    /**
     * {
     * "code": 0,
     * "msg": "success",
     * "app_access_token": "a-6U1SbDiM6XIH2DcTCPyeub",
     * "expire": 7140
     * }
     */
    @Override
    public SysConnAccessToken fetchAccessToken( ) {
        log.info("getAccessToken...");


        if (accessToken != null && accessToken.isValid()) {
            log.info("Get from cache accessToken:{}", accessToken);
            return accessToken;
        }


        FSAccessTokenReq appCert = new FSAccessTokenReq();
        appCert.setAppId(conf.getCertKey());
        appCert.setAppSecret(conf.getCertSecret());

        RestClient client = getClient();

        FSAccessTokenRes res = client.post(
                getRequestUrl("/auth/v3/app_access_token/internal"),
                FSAccessTokenRes.class,
                appCert,
                null);

        log.info("请求token=>{}", res);

        OkAssertConnector.success(getType(), res, "获取AccessToken信息");

        SysConnAccessToken r = res.to(conf);


        return r;
    }

    @Override
    public List<Department> getDepartmentList(String parentId) {
        log.info("getDepartmentList...");


        log.info("parentId:{}", parentId);

//
//        String url = getRequestUrl(
//                String.format("/contact/v3/departments?department_id_type=open_department_id&fetch_child=%s＆parent_department_id＝%s",
//                false, parentId));
//        log.info("url:{}", url);
//
//        // create headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken.getAccessToken());
//
//        // create request
//        HttpEntity request = new HttpEntity(headers);
//
//        ResponseEntity<FSDepartmentRes> re = restTemplate.exchange(
//                url,
//                HttpMethod.GET,
//                request,
//                FSDepartmentRes.class);
//        log.info("请求departments=>{}", re);
//
//        FSDepartmentRes res = re.getBody();
//        OkAssertConnector.success(getType(), res, "获取部门信息");
        return null;
//        return res.to(app);
    }

    @Override
    public List<SysConUser> getUserIdList(OrgIntegrateConf app, String deptId) {
        return null;
    }

    @Override
    public SysConnUserInfo getUserInfoList(OrgIntegrateConf app, String deptId) {
        return null;
    }
}
