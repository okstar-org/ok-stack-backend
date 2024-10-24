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

package org.okstar.platform.system.storage;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.system.conf.domain.SysConfIntegration;
import org.okstar.platform.system.conf.domain.SysConfIntegrationMinio;
import org.okstar.platform.system.conf.service.SysConfIntegrationService;

@ApplicationScoped
public class StorageManagerImpl implements StorageManager {

    @Inject
    SysConfIntegrationService sysConfIntegrationService;

    @Override
    public StorageBackend getDefaultStorageBackend() {
        SysConfIntegration integration = sysConfIntegrationService.find();
        SysConfIntegrationMinio minio = integration.getMinio();
        return new StorageBackendMinio(minio);
    }
}
