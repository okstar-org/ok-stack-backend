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

package org.okstar.platform.tenant.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.bson.BsonArray;
import org.bson.Document;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.tenant.doc.TenantMetaDoc;

import java.util.Objects;

@ApplicationScoped
public class MetaDocMapper {

    @Inject
    MongoClient mongoClient;

    @Inject
    ObjectMapper objectMapper;


    public TenantMetaDoc loadMetaDoc(Long tenantId) {
        TenantMetaDoc metaDTO = getMetaDoc(tenantId);
        if (metaDTO == null) {
            metaDTO = new TenantMetaDoc();
            metaDTO.setTenantId(tenantId);
            String insertId = insert(metaDTO);
            Log.infof("Inserted tenant meta => %s", insertId);
        }
        return metaDTO;
    }

    @SneakyThrows(JsonProcessingException.class)
    private String insert(TenantMetaDoc metaDTO) {
        OkAssert.notNull(metaDTO.getTenantId(), "tenantId is required");
        MongoCollection<Document> collection = getMetaCollection();
        InsertOneResult result = collection.insertOne(Document.parse(objectMapper.writeValueAsString(metaDTO)));
        return Objects.requireNonNull(result.getInsertedId()).toString();
    }

    @SneakyThrows(JsonProcessingException.class)
    public Long update(TenantMetaDoc metaDTO) {
        OkAssert.notNull(metaDTO.getTenantId(), "tenantId is required");
        UpdateResult result = getMetaCollection().updateOne(//
                Filters.eq("tenantId", metaDTO.getTenantId()),//
                Updates.set("dbs", BsonArray.parse(objectMapper.writeValueAsString(metaDTO.getDbs()))));
        return Objects.requireNonNull(result.getMatchedCount());
    }

    public TenantMetaDoc getMetaDoc(Long tenantId) {
        MongoCollection<Document> collection = getMetaCollection();
        return collection
                .find(Filters.eq("tenantId", tenantId), TenantMetaDoc.class)
                .first();
    }

    private MongoCollection<Document> getMetaCollection() {
        return mongoClient
                .getDatabase("module-tenant")
                .getCollection("tenantMeta");
    }


}
