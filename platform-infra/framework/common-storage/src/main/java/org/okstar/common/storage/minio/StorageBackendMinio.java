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

package org.okstar.common.storage.minio;

import io.minio.*;
import io.minio.messages.Item;
import io.minio.messages.Tags;
import io.quarkus.logging.Log;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.okstar.common.storage.StorageBackend;
import org.okstar.platform.common.exception.OkRuntimeException;

import java.io.InputStream;
import java.util.*;

/**
 * Minio实现
 */
public class StorageBackendMinio implements StorageBackend {
    /**
     * Minio配置
     */
    StorageConfMinio minio;

    public StorageBackendMinio(StorageConfMinio minio) {
        this.minio = minio;
    }

    /**
     * 通过Tag删除
     * @param bucketName
     * @param tags
     * @return
     * @throws OkRuntimeException
     */
    @Override
    public Set<String> removeByTags(@Nonnull String bucketName, @Nonnull Map<String, String> tags) throws OkRuntimeException {
        Set<String> deleted = new HashSet<>();
        try {
            MinioClient client = createClient();
            for (Result<Item> obj : client.listObjects(ListObjectsArgs.builder().bucket(bucketName).build())) {
                Item item = obj.get();
                if (item.isDir()) {
                    continue;
                }

                String objectName = item.objectName();

                Tags tags0 = client.getObjectTags(
                        GetObjectTagsArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());

                for (Map.Entry<String, String> entry : tags.entrySet()) {
                    String k = entry.getKey();
                    String v = entry.getValue();
                    if (Objects.equals(tags0.get().get(k), v)) {
                        client.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
                        deleted.add(objectName);
                    }
                }
            }
        } catch (Exception e) {
            Log.errorf(e, "error:%s", e.getMessage());
            throw new OkRuntimeException("Remove object:%s".formatted(e.getMessage()));
        }
        return deleted;
    }

    /**
     * 存储对象
     * @param bucketName
     * @param inputPart
     * @param name
     * @param tags
     * @return
     * @throws OkRuntimeException
     */
    @Override
    public String put(@Nonnull String bucketName,
                      @Nonnull InputPart inputPart,
                      @Nullable String name,
                      @Nullable Map<String, String> tags) throws OkRuntimeException {
        try (InputStream inputStream = inputPart.getBody()) {
            MinioClient client = createClient();

            boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            String contentType = String.valueOf(inputPart.getMediaType());
            int indexOf = contentType.indexOf(";");
            if (0 < indexOf) {
                contentType = contentType.substring(0, indexOf);
            }

            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(Optional.ofNullable(name).orElse(inputPart.getFileName()))
                    .tags(tags)
                    .stream(inputStream, -1, 5 * 1024 * 1024)
                    .contentType(contentType)
                    .build();


            ObjectWriteResponse response = client.putObject(objectArgs);

            String url = minio.getValidExternalUrl() + "/" + bucketName + "/" + response.object();
            Log.infof("Storage object:[%s]=> etag:%s url: %s", name, response.etag(), url);

            return url;

        } catch (Exception e) {
            Log.errorf(e, "error:%s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建Minio客户端
     * @return
     */
    private MinioClient createClient() {
        MinioClient.Builder minioClientBuilder = MinioClient.builder();
        minioClientBuilder.endpoint(minio.getEndpoint());
        minioClientBuilder.credentials(minio.getAccessKey(), minio.getSecretKey());
        return minioClientBuilder.build();
    }

}
