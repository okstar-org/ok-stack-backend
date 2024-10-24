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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.io.FilenameUtils;
import org.okstar.platform.system.dto.UploadDTO;
import org.okstar.platform.system.storage.StorageBackend;
import org.okstar.platform.system.storage.StorageManager;

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
        return backend.put(BUCKET_NAME, uploadDTO.getFile(), name);
    }

    @Override
    public String uploadLogo(UploadDTO uploadDTO) {
        String name = "logo.%s".formatted(FilenameUtils.getExtension(uploadDTO.getFile().getFileName()));
        StorageBackend backend = storageManager.getDefaultStorageBackend();
        return backend.put(BUCKET_NAME, uploadDTO.getFile(), name);
    }

}