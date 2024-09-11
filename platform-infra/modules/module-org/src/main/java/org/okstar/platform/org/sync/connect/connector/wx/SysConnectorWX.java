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

package org.okstar.platform.org.sync.connect.connector.wx;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.web.rest.transport.RestClient;
import org.okstar.platform.org.sync.connect.SysConEnums;
import org.okstar.platform.org.sync.connect.connector.SysConnectorAbstract;
import org.okstar.platform.org.sync.connect.connector.common.OkAssertConnector;
import org.okstar.platform.org.sync.connect.connector.wx.proto.access.req.WXAccessTokenReq;
import org.okstar.platform.org.sync.connect.connector.wx.proto.access.res.WXAccessTokenRes;
import org.okstar.platform.org.sync.connect.connector.wx.proto.department.res.WXDepartmentRes;
import org.okstar.platform.org.sync.connect.connector.wx.proto.department.res.WXUserListRes;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.dto.SysConUser;
import org.okstar.platform.org.sync.connect.proto.SysConnAccessToken;
import org.okstar.platform.org.sync.connect.proto.SysConnDepartment;
import org.okstar.platform.org.sync.connect.proto.SysConnUserInfo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class SysConnectorWX extends SysConnectorAbstract {


    @Override
    public SysConEnums.SysConType getType() {
        return SysConEnums.SysConType.WX;
    }

    @Override
    public String getBaseUrl() {
        return "https://qyapi.weixin.qq.com/cgi-bin";
    }

    /**
     * 参考链接：
     * https://work.weixin.qq.com/api/doc/10013#%E7%AC%AC%E4%B8%89%E6%AD%A5%EF%BC%9A%E8%8E%B7%E5%8F%96access_token
     * <p>
     * 请求方式：GET（HTTPS）
     * 请求URL：https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=ID&corpsecret=SECRET
     *
     * @param app
     * @return
     */
    @Override
    public SysConnAccessToken getAccessToken(OrgIntegrateConf app) {

        log.info("getAccessToken...");
        log.info("app:{}", app);

        OkAssert.notNull(app, "appCert is null!");


        if (accessToken != null && accessToken.isValid()) {
            return accessToken;
        }

        WXAccessTokenReq req = WXAccessTokenReq.builder().build().fromAppCert(app);

        log.info("getAccessToken:{}...", req);

        RestClient client = getClient();


        Map<String, String> params = new LinkedHashMap<>();
        params.put("corpid", req.getCorpId());
        params.put("corpsecret", req.getCorpSecret());

        WXAccessTokenRes res = client.get("/gettoken ",
                WXAccessTokenRes.class, params);

        log.info("res=>{}", res);

        OkAssertConnector.success(getType(), res, "获取认证信息！");
        SysConnAccessToken r = res.to(app);

        log.info("getAccessToken=>{}", r);


        return r;
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
    public List<SysConnDepartment> getDepartmentList(OrgIntegrateConf app, String parentId) {
        log.info("getDepartmentList...");
        log.info("parentId:{}", parentId);


        OkAssert.notNull(accessToken, "accessToken");

        String url = "/department/list";
        log.info("req=>{}", url);

        RestClient client = getClient();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("corpid", parentId);
        params.put("corpsecret", accessToken.getAccessToken());

        WXDepartmentRes res = client.get(url, WXDepartmentRes.class, params);
        log.info("res=>{}", res);

        OkAssertConnector.success(getType(), res, "获取部门信息");

        return res.to(app);
    }

    @Override
    public List<SysConUser> getUserIdList(OrgIntegrateConf app, String deptId) {
        log.info("getUserIdList...");
        log.info("deptId:{}", deptId);

        OkAssert.notNull(accessToken, "accessToken");

        String url = "/user/list ";

        log.info("req=>{}", url);
        Map<String, String> ps = new LinkedHashMap<>();
        ps.put("access_token", accessToken.getAccessToken());
        ps.put("department_id", deptId);
        ps.put("fetch_child", String.valueOf(0));
        WXUserListRes res = getClient().get("/user/list", WXUserListRes.class, ps);
        log.info("res=>{}", res);

        OkAssertConnector.success(getType(), res, "获取用户列表信息");

        return res.to(app);
    }

    @Override
    public SysConnUserInfo getUserInfoList(OrgIntegrateConf app, String userId) {
        throw new NotSupportedException();
    }
}
