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

import io.minio.*;
import io.quarkus.logging.Log;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.okstar.platform.common.exception.OkRuntimeException;
import org.okstar.platform.system.conf.domain.SysConfIntegrationMinio;

import java.io.InputStream;
import java.util.Optional;

public class StorageBackendMinio implements StorageBackend {

    SysConfIntegrationMinio minio;

    StorageBackendMinio(SysConfIntegrationMinio minio) {
        this.minio = minio;
    }


    @Override
    public String put(String bucketName, InputPart inputPart, String name) throws OkRuntimeException {
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
                    .stream(inputStream, -1, 5 * 1024 * 1024)
                    .contentType(contentType)
                    .build();

            ObjectWriteResponse response = client.putObject(objectArgs);
            return minio.getValidExternalUrl() + "/" + bucketName + "/" + response.object();

        } catch (Exception e) {
            Log.errorf(e, "error:%s", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private MinioClient createClient() {
        MinioClient.Builder minioClientBuilder = MinioClient.builder();
        minioClientBuilder.endpoint(minio.getEndpoint());
        minioClientBuilder.credentials(minio.getAccessKey(), minio.getSecretKey());
        return minioClientBuilder.build();
    }

}