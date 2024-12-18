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

package org.okstar.platform.org.connect.connector;


import org.okstar.platform.org.connect.ConnectorDefines;
import org.okstar.platform.org.connect.api.AccessToken;
import org.okstar.platform.org.connect.api.Department;
import org.okstar.platform.org.connect.api.UserId;
import org.okstar.platform.org.connect.api.UserInfo;
import org.okstar.platform.org.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.connect.exception.ConnectorException;

import java.util.List;

/**
 * `组织模块`第三方连接器接口
 */
public interface OrgConnector {

    OrgIntegrateConf getConf();

    /**
     * 获取保证有效的token
     * @return
     * @throws ConnectorException
     */
    AccessToken ensureAccessToken() throws ConnectorException;

    ConnectorDefines.Type getType();

    String getRequestUrl(String url);

    void setAccessToken(AccessToken accessToken);

    /**
     * 获取AccessToken
     * @return SysConnAccessToken
     */
    AccessToken fetchAccessToken() throws ConnectorException;

    /**
     * 获取跟部门
     * @return
     */
    Department getDepartment(String id) throws ConnectorException;

    /**
     * 获取部门列表
     * @param parentId
     * @return
     */
    List<Department> getDepartmentList(String parentId) throws ConnectorException;

    /**
     * 获取部门下的用户
     *
     * @param dept
     * @return
     */
    List<UserId> getUserIdList(Department dept) throws ConnectorException;

    /**
     * 获取部门用户信息
     * @param userId
     * @return
     */
    UserInfo getUserInfo(String userId) throws ConnectorException;
}
