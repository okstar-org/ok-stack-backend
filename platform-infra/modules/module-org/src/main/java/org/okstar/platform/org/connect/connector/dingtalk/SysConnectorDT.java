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

package org.okstar.platform.org.connect.connector.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2DepartmentListidsRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2DepartmentListidsResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoResponse;
import io.quarkus.logging.Log;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.json.OkJsonUtils;
import org.okstar.platform.common.web.OkRestUtil;
import org.okstar.platform.common.web.rest.transport.RestClient;
import org.okstar.platform.org.connect.api.AccessToken;
import org.okstar.platform.org.connect.api.Department;
import org.okstar.platform.org.connect.api.UserId;
import org.okstar.platform.org.connect.api.UserInfo;
import org.okstar.platform.org.connect.exception.ConnectorException;
import org.okstar.platform.org.connect.connector.SysConnectorAbstract;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SysConnectorDT extends SysConnectorAbstract {


    public SysConnectorDT(OrgIntegrateConf conf) {
        this.conf = conf;
    }

    @Override
    public AccessToken fetchAccessToken() throws ConnectorException {
        String url = "/gettoken";
        try {
            log.info("Configuration: {}", conf);

            DingTalkClient client = new DefaultDingTalkClient(getRequestUrl(url));
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(conf.getCertKey());
            request.setAppsecret(conf.getCertSecret());
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            OkAssert.notNull(response, "response is null");

            log.info("getAccessToken=>{}", response.getBody());

            OkAssert.isTrue(response.getErrcode() == 0, "获取token异常！");

            var accessToken = AccessToken.builder()
                    .accessToken(response.getAccessToken())
                    .expiresIn(response.getExpiresIn())
                    .build();
            setAccessToken(accessToken);
            return accessToken;
        } catch (ApiException e) {
            throw new ConnectorException(conf.getType(), url, e.getCause());
        }
    }

    @Override
    public List<Department> getDepartmentList(String parentId) throws ConnectorException {
        String url = "/topapi/v2/department/listsub?dept_id=" + parentId;
        List<Department> list = Lists.newArrayList();
        try {
            log.info("getDepartmentList parentId:{}", parentId);

            //https://open.dingtalk.com/document/orgapp/obtain-the-department-list-v2

            DingTalkClient client = new DefaultDingTalkClient(getRequestUrl(url));
            OapiV2DepartmentListidsRequest req = new OapiV2DepartmentListidsRequest();
            req.setId(Long.valueOf(parentId));

            OapiV2DepartmentListidsResponse response = client.execute(req, ensureAccessToken().getAccessToken());
            log.info("getDepartmentList=>{}", OkJsonUtils.asString(response));

            assertResponse(response);

            String body = response.getBody();

            ObjectNode node = OkJsonUtils.asObject(body, ObjectNode.class);
            node.get("result").elements().forEachRemaining(e -> {
                //{"auto_add_user":true,"create_dept_group":true,"dept_id":551835489,"name":"研发部","parent_id":1}
                Log.infof("item:%s", e);
                Department dept = Department.builder()
                        .id(e.get("dept_id").asText())
                        .name(e.get("name").asText())
                        .parentId(e.get("parent_id").asText())
                        .build();
                list.add(dept);
            });
        } catch (ApiException e) {
            throw new ConnectorException(conf.getType(), url, e.getCause());
        }
        return list;
    }

    /**
     * @param dept
     * @return
     * @throws ConnectorException
     */
    @Override
    public List<UserId> getUserIdList(Department dept) throws ConnectorException {

        String url = "/topapi/user/listid";

        log.info("getUserIdList dept:{}", dept.getName());

        // 参考： https://open.dingtalk.com/document/orgapp/query-the-list-of-department-userids

        RestClient restClient = OkRestUtil.getInstance(getRequestUrl("")).getClientFactory().createClient();

        Map<String, String> map = new HashMap<>();
        map.put("dept_id", dept.getId());
        map.put("access_token", ensureAccessToken().getAccessToken());

        String response = restClient.get(url, String.class, map);
        Log.infof("response=> %s", response);

        ObjectNode node = OkJsonUtils.asObject(response, ObjectNode.class);

        //check
        String errcode = node.get("errcode").asText();
        OkAssert.isTrue("0".equals(errcode), String.format("【%s】连接异常，errcode:%s errmsg:%s", getType().getText(), node.get("errcode").asText(), node.get("errmsg").asText()));

        List<UserId> list = Lists.newArrayList();
        node.get("result").get("userid_list").elements().forEachRemaining(e -> {
            list.add(UserId.builder().userId(e.asText()).build());
        });

        return list;
    }

    @Override
    public UserInfo getUserInfo(String userId) throws ConnectorException {
        String url = "/topapi/v2/user/get";
        try {
            log.info("getUserInfo userId: {}", userId);

            DingTalkClient client = new DefaultDingTalkClient(getRequestUrl(url));
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);

            OapiV2UserGetResponse rsp = client.execute(req, ensureAccessToken().getAccessToken());

            OapiV2UserGetResponse.PageResult e = rsp.getResult();
            log.info("getUserInfo:[{}]=> {}", userId, OkJsonUtils.asString(e));

            return UserInfo.builder()
                    .id(e.getUserid())
                    .name(e.getName())
                    .avatar(e.getAvatar())
                    .nickname(e.getNickname())
                    .unionId(e.getUnionid())
                    .isBoos(e.getBoss())
                    .isActive(e.getActive())
                    .isAdmin(e.getAdmin())
                    .email(e.getEmail())
                    .orgMail(e.getOrgEmail())
                    .mobilePhone(e.getMobile())
                    .title(e.getPosition())
                    .build();
        } catch (ApiException e) {
            throw new ConnectorException(getType(), url, e.getCause());
        }
    }

    private void assertResponse(TaobaoResponse response) {
        OkAssert.isTrue("0".equals(response.getErrorCode()),
                String.format("【%s】连接异常，subCode:%s subMsg:%s",
                        getType().getText(), response.getSubCode(), response.getSubMsg()));
    }
}
