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

package org.okstar.platform.tenant.service;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.tenant.entity.MetaEntity;
import org.okstar.platform.tenant.entity.MetaEntity_;
import org.okstar.platform.tenant.repo.MetaMapper;

import java.util.List;

@Transactional
@ApplicationScoped
public class MetaServiceImpl implements MetaService {
    @Inject
    MetaMapper metaMapper;

    @Override
    public void save(MetaEntity metaEntity) {
        metaMapper.persist(metaEntity);
    }

    @Override
    public List<MetaEntity> findAll() {
        return metaMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<MetaEntity> findPage(OkPageable page) {
        var paged = metaMapper
                .findAll(Sort.descending(MetaEntity_.ID))
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());
    }

    @Override
    public MetaEntity get(Long id) {
        return metaMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        metaMapper.deleteById(id);
    }

    @Override
    public void delete(MetaEntity metaEntity) {
        metaMapper.delete(metaEntity);
    }

    @Override
    public MetaEntity loadByTenant(Long tenantId) {
        return metaMapper.find(MetaEntity_.TENANT_ID, tenantId).stream()
                .findFirst().or(() -> {
                    var metaEntity = new MetaEntity();
                    metaEntity.setTenantId(tenantId);
                    metaMapper.persist(metaEntity);
                    return java.util.Optional.of(metaEntity);
                }).get();
    }
}
