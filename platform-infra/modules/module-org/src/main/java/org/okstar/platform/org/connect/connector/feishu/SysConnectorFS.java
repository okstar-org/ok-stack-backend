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

package org.okstar.platform.org.connect.connector.feishu;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.json.OkJsonUtils;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.common.web.auth.AuthenticationToken;
import org.okstar.platform.common.web.rest.transport.RestClient;
import org.okstar.platform.core.user.UserDefines;
import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.connect.api.AccessToken;
import org.okstar.platform.org.connect.api.Department;
import org.okstar.platform.org.connect.api.UserId;
import org.okstar.platform.org.connect.api.UserInfo;
import org.okstar.platform.org.connect.exception.ConnectorException;
import org.okstar.platform.org.connect.connector.SysConnectorAbstract;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;

import java.util.*;

@Slf4j
public class SysConnectorFS extends SysConnectorAbstract {


    public SysConnectorFS(OrgIntegrateConf conf) {
        this.conf = conf;
    }

    @Override
    public ConnectorDefines.Type getType() {
        return ConnectorDefines.Type.FS;
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
    public AccessToken fetchAccessToken() throws ConnectorException {
        String url = "auth/v3/app_access_token/internal";
        try {
            ObjectNode req = new ObjectMapper().createObjectNode();
            req.put("app_id", conf.getAppId());
            req.put("app_secret", conf.getCertSecret());

            log.info("getAccessToken: {} {}", conf.getAppId(),
                    OkStringUtil.abbreviate(conf.getCertSecret(), 20));

            RestClient client = getClient();

            //https://open.feishu.cn/document/server-docs/authentication-management/access-token/app_access_token_internal
            String res = client.post(
                    url,
                    String.class,
                    req.toString(),
                    null);

            log.info("getAccessToken: {} => {}", conf.getAppId(), res);
            ObjectNode node = OkJsonUtils.asObject(res, ObjectNode.class);
            OkAssert.isTrue(node.get("code").asInt() == 0, "返回异常！");

            /**
             * {
             *     "app_access_token": "t-g1044ghJRUIJJ5ZPPZMOHKWZISL33E4QSS3abcef",
             *     "code": 0,
             *     "expire": 7200,
             *     "msg": "ok",
             *     "tenant_access_token": "t-g1044ghJRUIJJ5ZPPZMOHKWZISL33E4QSS3abcef"
             * }
             */

            var accessToken = AccessToken.builder()
                    .accessToken(node.get("app_access_token").asText())
                    .expiresIn(node.get("expire").asLong())
                    .build();

            setAccessToken(accessToken);

            return accessToken;
        } catch (Exception e) {
            throw new ConnectorException(conf.getType(), url, e.getCause());
        }
    }

    @Override
    public List<Department> getDepartmentList(String parentId) throws ConnectorException {
        log.info("getDepartmentList parentId:{}", parentId);

        String url = "/contact/v3/departments";
        log.info("url:{}", url);

        Map<String, String> params = new HashMap<>();
        params.put("parent_department_id", parentId);
        params.put("fetch_child", "false");
        params.put("department_id_type", "open_department_id");

        RestClient client = getClient();
        client.setToken(new AuthenticationToken(ensureAccessToken().getAccessToken()));

        String res = client.get(url, String.class, params);
        log.info("response=> {}", res);

        ObjectNode node = OkJsonUtils.asObject(res, ObjectNode.class);
        OkAssert.isTrue(0 == node.get("code").asInt(), "返回异常！");

        ArrayList<Department> list = new ArrayList<>();
        JsonNode jsonNode = node.get("data").get("items");
        if (jsonNode != null) {
            jsonNode.elements().forEachRemaining(depart -> {
                Department department = Department.builder()
                        .id(depart.get("open_department_id").asText())
                        .name(depart.get("name").asText())
                        .parentId(depart.get("parent_department_id").asText())
                        .build();
                list.add(department);
            });
        }
        return list;

    }

    @Override
    public List<UserId> getUserIdList(Department dept) throws ConnectorException {
        log.info("getUserIdList:{}", dept.getName());

        String url = "contact/v3/users/find_by_department";
        log.info("url:{}", url);

        RestClient client = getClient();
        client.setToken(new AuthenticationToken(ensureAccessToken().getAccessToken()));

        Map<String, String> params = new HashMap<>();
        params.put("user_id_type", "open_id");
        params.put("page_size", "10");
        params.put("department_id_type", "open_department_id");
        params.put("department_id", dept.getId());

        ArrayList<UserId> list = new ArrayList<>();

        String pageToken = null;
        ObjectNode node = null;
        do {
            if (pageToken != null) {
                params.put("page_token", pageToken);
            }

            String res = client.get(url, String.class, params);
            log.info("response=> {}", res);

            node = OkJsonUtils.asObject(res, ObjectNode.class);
            OkAssert.isTrue(0 == node.get("code").asInt(), "返回异常！");

            JsonNode items = node.get("data").get("items");
            if (items != null) {
                items.elements().forEachRemaining(depart -> {
                    var department = UserId.builder()
                            .userId(depart.get("union_id").asText())
                            .build();
                    list.add(department);
                });
            }

            var tokenNode = node.get("data").get("page_token");
            if (tokenNode != null) {
                pageToken = tokenNode.asText();
            }
        } while ((node.get("data").get("has_more").asBoolean()));

        return list;
    }

    @Override
    public UserInfo getUserInfo(String userId) throws ConnectorException {

        log.info("getUserInfo:{}", userId);

        String url = "contact/v3/users/" + userId;
        log.info("url:{}", url);

        RestClient client = getClient();
        client.setToken(new AuthenticationToken(ensureAccessToken().getAccessToken()));

        Map<String, String> params = new HashMap<>();
        params.put("user_id_type", "union_id");
        params.put("department_id_type", "open_department_id");


        //https://open.feishu.cn/document/server-docs/contact-v3/user/get
        String res = client.get(url, String.class, params);
        log.info("response=> {}", res);

        ObjectNode node = OkJsonUtils.asObject(res, ObjectNode.class);
        OkAssert.isTrue(0 == node.get("code").asInt(), "返回异常！");

        JsonNode data = node.get("data");
        JsonNode user;
        if (data != null && (user = data.get("user")) != null) {
            UserInfo info = UserInfo.builder()
                    //获取用户 user ID contact:user.employee_id:readonly
                    .id(Optional.ofNullable(user.get("user_id")).map(JsonNode::asText).orElse(null))

                    //基本信息 contact:user.base:readonly
                    .name(user.get("name").asText())
                    .avatar(user.get("avatar").asText("avatar_origin"))
                    .nickname(Optional.ofNullable(user.get("nickname")).map(JsonNode::asText).orElse(null))
                    .unionId(user.get("union_id").asText())

                    //获取用户邮箱信息
                    .email(user.get("email").asText())
                    //获取用户手机号
                    .mobilePhone(user.get("mobile").asText())

                    //获取用户受雇信息 contact:user.employee:readonly
                    .title(user.get("job_title").asText())
                    .orgMail(Optional.ofNullable(user.get("enterprise_email")).map(JsonNode::asText).orElse(null))
                    .isActive(user.get("status").get("is_activated").asBoolean())
                    .build();

            //获取用户性别 contact:user.gender:readonly 0：保密//1：男//2：女//3：其他
            var g = switch (user.get("gender").asInt()) {
                case 1 -> UserDefines.Gender.male;
                case 2 -> UserDefines.Gender.female;
                default -> UserDefines.Gender.none;
            };

            info.setGender(g);

            return info;

        }


        return null;
    }
}
