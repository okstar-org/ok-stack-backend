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

package org.okstar.platform.org.connect.connector.wx;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.json.OkJsonUtils;
import org.okstar.platform.common.web.rest.transport.RestClient;
import org.okstar.platform.org.connect.api.AccessToken;
import org.okstar.platform.org.connect.api.Department;
import org.okstar.platform.org.connect.api.UserId;
import org.okstar.platform.org.connect.api.UserInfo;
import org.okstar.platform.org.connect.connector.SysConnectorAbstract;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.connect.exception.ConnectorException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class SysConnectorWX extends SysConnectorAbstract {


    public SysConnectorWX(OrgIntegrateConf conf) {
        this.conf = conf;
    }

    /**
     * 参考链接：
     * https://work.weixin.qq.com/api/doc/10013#%E7%AC%AC%E4%B8%89%E6%AD%A5%EF%BC%9A%E8%8E%B7%E5%8F%96access_token
     * <p>
     * 请求方式：GET（HTTPS）
     * 请求URL：https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET
     *
     * @return
     */
    @Override
    public AccessToken fetchAccessToken() throws ConnectorException {
        OkAssert.notNull(conf, "conf is null!");

        log.info("getAccessToken...");

        String url = "/gettoken";

        Map<String, String> params = new LinkedHashMap<>();
        params.put("corpid", conf.getCertKey());
        params.put("corpsecret", conf.getCertSecret());
        log.info("getAccessToken:{}...", params);

        RestClient client = getClient();

        String res = client.get(url, String.class, params);
        log.info("res=>{}", res);
        /**
         * {
         *    "errcode": 0,
         *    "errmsg": "ok",
         *    "access_token": "accesstoken000001",
         *    "expires_in": 7200
         * }
         */
        ObjectNode node = OkJsonUtils.asObject(res, ObjectNode.class);
        if (0 != node.get("errcode").asInt()) {
            throw new ConnectorException(getType(), url, "获取Token异常！");
        }

        var accessToken = AccessToken.builder()
                .accessToken(node.get("access_token").asText())
                .expiresIn(node.get("expires_in").asLong())
                .build();

        setAccessToken(accessToken);

        log.info("getAccessToken=>{}", accessToken);
        return accessToken;
    }

    /**
     * {@inheritDoc}
     * 参考文档：https://work.weixin.qq.com/api/doc/90000/90135/90208
     * 获取部门列表
     * 调试工具
     * <p>
     * 请求方式：GET（HTTPS）
     * 请求地址：https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token=ACCESS_TOKEN&id=ID
     * <p>
     * 参数说明 ：
     * 参数 	必须 	说明
     * access_token 	是 	调用接口凭证
     * id 	否 	部门id。获取指定部门及其下的子部门（以及及子部门的子部门等等，递归）。 如果不填，默认获取全量组织架构
     * <p>
     * 权限说明：
     * <p>
     * 只能拉取token对应的应用的权限范围内的部门列表
     *
     * @param parentId
     * @return
     */
    @Override
    public List<Department> getDepartmentList(String parentId) throws ConnectorException {
        log.info("getDepartmentList parentId:{}", parentId);


        String url = "/department/list";
        log.info("req=>{}", url);

        RestClient client = getClient();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("id", parentId);
        params.put("access_token", ensureAccessToken().getAccessToken());

        var res = client.get(url, String.class, params);
        log.info("res=>{}", res);

        ObjectNode node = OkJsonUtils.asObject(res, ObjectNode.class);
        OkAssert.isTrue(0 == node.get("errcode").asInt(), "返回异常！");

        List<Department> list = Lists.newArrayList();

        JsonNode departments = node.get("department");
        if (departments != null) {
            departments.elements().forEachRemaining(e -> {
                Department dept = Department.builder()
                        .id(e.get("id").asText())
                        .name(e.get("name").asText())
                        .parentId(e.get("parentid").asText())
                        .build();
                list.add(dept);
            });
        }
        return list.stream().filter(e -> Objects.equals(e.getParentId(), parentId)).toList();
    }

    @Override
    public List<UserId> getUserIdList(Department dept) throws ConnectorException {
        log.info("getUserIdList...");
        log.info("deptId:{}", dept);


        String url = "/user/simplelist";
        log.info("req=>{}", url);

        Map<String, String> ps = new LinkedHashMap<>();
        ps.put("access_token", ensureAccessToken().getAccessToken());
        ps.put("department_id", dept.getId());
        ps.put("fetch_child", String.valueOf(0));

        String res = getClient().get(url, String.class, ps);
        log.info("res=>{}", res);

        ObjectNode node = OkJsonUtils.asObject(res, ObjectNode.class);
        OkAssert.isTrue(0 == node.get("errcode").asInt(), "返回异常！");

        List<UserId> list = Lists.newArrayList();
        JsonNode userlist = node.get("userlist");
        if (userlist != null) {
            userlist.elements().forEachRemaining(e -> {
                list.add(UserId.builder().userId(e.get("userid").asText()).build());
            });
        }
        return list;
    }

    @Override
    public UserInfo getUserInfo(String userId) throws ConnectorException {

        Map<String, String> ps = new LinkedHashMap<>();
        ps.put("access_token", ensureAccessToken().getAccessToken());
        ps.put("userid", userId);

        String url = "/user/get";

        String res = getClient().get(url, String.class, ps);
        log.info("res=>{}", res);

        ObjectNode node = OkJsonUtils.asObject(res, ObjectNode.class);
        OkAssert.isTrue(0 == node.get("errcode").asInt(), "返回异常！");

        return UserInfo.builder()
                .id(node.get("userid").asText())
                .name(node.get("name").asText())
                .avatar(node.get("avatar").asText())
                .nickname(node.get("alias").asText())
                .isActive(node.get("status").asInt() == 1)
                .email(node.get("email").asText())
                .orgMail(node.get("biz_mail").asText())
                .mobilePhone(node.get("mobile").asText())
                .linePhone(node.get("telephone").asText())
                .title(node.get("position").asText())
                .build();
    }
}
