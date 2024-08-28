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

package org.okstar.platform.system.settings.service;


import org.okstar.platform.common.core.OkService;
import org.okstar.platform.system.settings.domain.SysConfIntegration;
import org.okstar.platform.system.settings.domain.SysConfIntegrationIm;
import org.okstar.platform.system.settings.domain.SysConfIntegrationStack;

/**
 *集成配置
 */
public interface SysConfIntegrationService extends OkService
{
    SysConfIntegration find();

    void save(SysConfIntegration integration);

    void uploadConf(SysConfIntegration integration);
}
