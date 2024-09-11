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

package org.okstar.platform.org.sync.connect.connector.dingtalk;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiV2DepartmentListidsRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.request.OapiV2UserListbypageRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiV2DepartmentListidsResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListbypageResponse;
import com.google.common.collect.Lists;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.org.defined.StaffDefines;
import org.okstar.platform.org.sync.connect.SysConEnums;
import org.okstar.platform.org.sync.connect.connector.SysConnectorAbstract;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.dto.SysConUser;
import org.okstar.platform.org.sync.connect.proto.SysConnAccessToken;
import org.okstar.platform.org.sync.connect.proto.SysConnDepartment;
import org.okstar.platform.org.sync.connect.proto.SysConnUserInfo;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
//@ApplicationScoped
public class SysConnectorDT extends SysConnectorAbstract {

    @Override
    public SysConEnums.SysConType getType() {
        return SysConEnums.SysConType.DD;
    }

    @Override
    public String getBaseUrl() {
        return "https://oapi.dingtalk.com";
    }


    @Override
    public SysConnAccessToken getAccessToken(OrgIntegrateConf app) {
        try {

            log.info("Get access token: {}", app);

            DingTalkClient client = new DefaultDingTalkClient(getRequestUrl("/gettoken"));
            OapiGettokenRequest request = new OapiGettokenRequest();
            request.setAppkey(app.getCertKey());
            request.setAppsecret(app.getCertSecret());
            request.setHttpMethod("GET");
            OapiGettokenResponse response = client.execute(request);
            OkAssert.notNull(response, "response is null");

            log.info("getAccessToken=>{}", response.getBody());

            OkAssert.isTrue(response.getErrcode() == 0, "获取token异常！");

            accessToken = SysConnAccessToken.builder()
                    .accessToken(response.getAccessToken())
                    .expiresIn(response.getExpiresIn())
                    .build();

            accessToken.setCreatedAt(OkDateUtils.instant());
            accessToken.setAppId(app.getAppId());
            accessToken.setType(SysConEnums.SysConType.DD);

            return accessToken;
        } catch (ApiException e) {
            throw new OkRuntimeException("获取钉钉token异常！", e);
        }
    }

    @Override
    public List<SysConnDepartment> getDepartmentList(OrgIntegrateConf app, String parentId) {
        try {
            log.info("getDepartmentList...");
            log.info("parentId:{}", parentId);


            DingTalkClient client = new DefaultDingTalkClient(getRequestUrl("/topapi/v2/department/listsub"));
            OapiV2DepartmentListidsRequest req = new OapiV2DepartmentListidsRequest();
            req.setId(Long.valueOf(parentId));

            OapiV2DepartmentListidsResponse response = client.execute(req, accessToken.getAccessToken());
            log.info("getDepartmentList=>{}", response);

            assertResponse(response);

            var result = response.getSubDeptIdList();

            return result.stream()
                    .map(e -> {
                                var x = SysConnDepartment.builder()   //
                                        .id(String.valueOf(e))    //
                                        .build();
                                x.setType(SysConEnums.SysConType.DD);
                                x.setAppId(app.getAppId());
                                return x;
                            }
                    ).collect(Collectors.toList());

        } catch (ApiException e) {
            throw new OkRuntimeException("获取钉钉部门用户ID异常！", e);
        }
    }

    @Override
    public List<SysConUser> getUserIdList(OrgIntegrateConf app, String deptId) {

        try {
            log.info("getUserIdList...");

            log.info("deptId:{}", deptId);


            DingTalkClient client = new DefaultDingTalkClient(getRequestUrl("/topapi/v2/user/list"));

            long cursor = 0;
            long size = 100;

            List<OapiV2UserListbypageResponse.UserSimpleListResponse> list = Lists.newArrayList();

            OapiV2UserListbypageResponse.PageResult result;
            do {
                var req = new OapiV2UserListbypageRequest();
                req.setDeptId(Long.valueOf(deptId));
                req.setOffset(cursor);
                req.setSize(size);

                log.info("Fetch user offset: {} size: {}", cursor, size);
                var response = client.execute(req, accessToken.getAccessToken());
                assertResponse(response);
                result = response.getResult();

                List<OapiV2UserListbypageResponse.UserSimpleListResponse> resultList = result.getList();
                list.addAll(resultList);
            } while (BooleanUtils.isTrue(result.getHasMore()));

            return list.stream().map(s -> {
                SysConUser d = new SysConUser();
                OkBeanUtils.copyPropertiesTo(s, d);
                d.setUserId(s.getUserid());
                d.setUnionId(s.getUnionid());
                d.setAppId(app.getAppId());
                d.setSource(StaffDefines.Source.DD);
                return d;
            }).collect(Collectors.toList());

        } catch (ApiException e) {
            throw new OkRuntimeException("获取钉钉用户异常！", e);
        }
    }

    @Override
    public SysConnUserInfo getUserInfoList(OrgIntegrateConf app, String userId) {
        try {
            log.info("getUserInfo userId: {}", userId);

            DingTalkClient client = new DefaultDingTalkClient(getRequestUrl(""));
            OapiV2UserGetRequest req = new OapiV2UserGetRequest();
            req.setUserid(userId);

            OapiV2UserGetResponse rsp = client.execute(req, accessToken.getAccessToken());


            OapiV2UserGetResponse.PageResult e = rsp.getResult();

            var x = SysConnUserInfo.builder()
                    .id(e.getUserid())
                    .unionId(e.getUnionid())
                    .isBoos(e.getBoss())
                    .isActive(e.getActive())
                    .mail(e.getEmail())
                    .orgMail(e.getOrgEmail())
                    .name(e.getName())
                    .title(e.getPosition())
                    .remark(e.getRemark())
                    .jobNumber(e.getJobNumber())
                    .countryCode(e.getStateCode())
                    .mobilePhone(e.getMobile())
                    .build();

            x.setType(SysConEnums.SysConType.DD);
            x.setAppId(app.getAppId());
            return x;
        } catch (RuntimeException | ApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void assertResponse(TaobaoResponse response) {
        OkAssert.isTrue("0".equals(response.getErrorCode()),
                String.format("【%s】连接异常，subCode:%s subMsg:%s",
                        getType().getText(), response.getSubCode(), response.getSubMsg()));
    }
}
