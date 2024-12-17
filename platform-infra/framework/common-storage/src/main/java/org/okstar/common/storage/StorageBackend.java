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

package org.okstar.common.storage;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.okstar.platform.common.exception.OkRuntimeException;

import java.util.Map;
import java.util.Set;

/**
 * 存储后端接口
 */
public interface StorageBackend {
    Set<String> removeByTags(@Nonnull String bucketName, @Nonnull Map<String, String> tags) throws OkRuntimeException;

    String put(@Nonnull String bucketName,
               @Nullable InputPart inputPart,
               @Nullable String name,
               @Nullable Map<String, String> tags) throws OkRuntimeException;
}
