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

package org.okstar.platform.system.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.dto.UploadDTO;
import org.okstar.platform.system.storage.StorageBackend;
import org.okstar.platform.system.storage.StorageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Transactional
@ApplicationScoped
public class SysUploadServiceImpl implements SysUploadService {
    public static final String BUCKET_NAME = "ok-stack";
    @Inject
    StorageManager storageManager;


    @Override
    public String uploadFavicon(UploadDTO uploadDTO) {
        String name = "favicon.ico";
        StorageBackend backend = storageManager.getDefaultStorageBackend();
        Map<String, String> tags = Map.of("type", "favicon");
        Set<String> removed = backend.removeByTags(BUCKET_NAME, tags);
        Log.debugf("Removed:%s", removed);
        return backend.put(BUCKET_NAME, uploadDTO.getFile(), name, tags);
    }

    @Override
    public String uploadLogo(UploadDTO uploadDTO) {
        String name = "logo.%s".formatted(FilenameUtils.getExtension(uploadDTO.getFile().getFileName()));
        StorageBackend backend = storageManager.getDefaultStorageBackend();
        Map<String, String> tags = Map.of("type", "logo");
        Set<String> removed = backend.removeByTags(BUCKET_NAME, tags);
        Log.debugf("Removed:%s", removed);
        return backend.put(BUCKET_NAME, uploadDTO.getFile(), name, tags);
    }

    @Override
    public String uploadAvatar(SysAccount self, UploadDTO uploadDTO) {
        String name = "user/%s/logo.%s".formatted(self.getUsername(), FilenameUtils.getExtension(uploadDTO.getFile().getFileName()));

        StorageBackend backend = storageManager.getDefaultStorageBackend();
        Map<String, String> tags = new HashMap<>(Map.of("type", "avatar"));
        tags.put("user", self.getUsername());
        Set<String> removed = backend.removeByTags(BUCKET_NAME, tags);
        Log.debugf("Removed:%s", removed);
        return backend.put(BUCKET_NAME, uploadDTO.getFile(), name, tags);

    }

}
