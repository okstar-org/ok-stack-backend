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

package org.okstar.platform.work.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.PathParam;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.entity.AppDetailEntity;
import org.okstar.cloud.entity.AppEntities;
import org.okstar.cloud.entity.AppEntity;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.platform.common.core.defined.OkCloudDefines;
import org.okstar.platform.common.core.web.page.OkPageable;

@ApplicationScoped
public class WorkAppServiceImpl implements WorkAppService {

    OkCloudApiClient client;

    public WorkAppServiceImpl() {
        client = new OkCloudApiClient(OkCloudDefines.OK_CLOUD_API_STACK,
                new AuthenticationToken(OkCloudDefines.OK_CLOUD_USERNAME, OkCloudDefines.OK_CLOUD_PASSWORD));
    }

    @Override
    public AppEntities page(OkPageable pageable) {
        return client.getAppChannel().getApps(pageable);
    }

    @Override
    public AppEntity get(@PathParam("id") Long id) {
        return client.getAppChannel().getApp(id);
    }

    @Override
    public AppDetailEntity detail(@PathParam("id") Long id) {
        return client.getAppChannel().getDetail(id);
    }
}
