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

package org.okstar.platform.system.conf.service;


import org.okstar.platform.core.service.OkService;
import org.okstar.platform.system.conf.domain.*;

/**
 * 集成配置服务接口
 */
public interface SysConfIntegrationService extends OkService
{
    SysConfIntegration find();

    void save(SysConfItem item);

    void save(SysConfIntegration integration);

    void uploadConf(SysConfIntegration integration);

    boolean testKeycloak(SysConfIntegrationKeycloak conf);

    boolean testIm(SysConfIntegrationIm conf);

    boolean testStack(SysConfIntegrationStack conf);

    boolean testMinio(SysConfIntegrationMinio conf);
}
