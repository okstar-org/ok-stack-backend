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

package org.okstar.platform.org.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.common.storage.StorageBackend;
import org.okstar.common.storage.StorageManager;
import org.okstar.common.storage.dto.UploadDTO;
import org.okstar.common.storage.minio.StorageConfMinio;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.system.rpc.SysConfIntegrationRpc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Transactional
@ApplicationScoped
public class OrgUploadServiceImpl implements OrgUploadService {
    public static final String BUCKET_NAME = "ok-stack";
    @Inject
    StorageManager storageManager;
    @Inject
    @RestClient
    SysConfIntegrationRpc sysConfIntegrationRpc;

    @Override
    public StorageConfMinio getConfig() {
        var integration = sysConfIntegrationRpc.getIntegrationConf();
        var minio = integration.getStorage();
        return StorageConfMinio.builder().endpoint(minio.getEndpoint())
                .accessKey(minio.getAccessKey())
                .secretKey(minio.getSecretKey())
                .externalUrl(minio.getExternalUrl())
                .build();
    }

    @Override
    public String uploadAvatar(Org self, UploadDTO uploadDTO) {
        String name = "org/%s/logo.%s".formatted(self.getUuid(), FilenameUtils.getExtension(uploadDTO.getFile().getFileName()));
        StorageBackend backend = storageManager.getDefaultStorageBackend(getConfig());
        Map<String, String> tags = new HashMap<>(Map.of("type", "avatar"));
        tags.put("org", self.getUuid());
        Set<String> removed = backend.removeByTags(BUCKET_NAME, tags);
        Log.debugf("Removed:%s", removed);
        return backend.put(BUCKET_NAME, uploadDTO.getFile(), name, tags);

    }

}
