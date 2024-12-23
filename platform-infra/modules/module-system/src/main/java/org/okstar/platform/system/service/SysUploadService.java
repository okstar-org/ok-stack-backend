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

import org.okstar.common.storage.dto.UploadDTO;
import org.okstar.common.storage.minio.StorageConfMinio;
import org.okstar.platform.system.account.domain.SysAccount;


public interface SysUploadService {

    StorageConfMinio getConfig();

    String uploadFavicon(UploadDTO uploadDTO);

    String uploadLogo(UploadDTO uploadDTO);

    /**
     * update user's avatar
     * @param self
     * @param uploadDTO
     * @return url
     */
    String uploadAvatar(SysAccount self, UploadDTO uploadDTO);
}
