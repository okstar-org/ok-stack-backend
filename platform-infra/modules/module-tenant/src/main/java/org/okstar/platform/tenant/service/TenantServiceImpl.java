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

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.tenant.entity.TenantEntity;
import org.okstar.platform.tenant.repo.TenantMapper;

import java.util.List;

@Transactional
@ApplicationScoped
public class TenantServiceImpl implements TenantService {
    @Inject
    TenantMapper tenantMapper;

    @Override
    public void save(TenantEntity tenantEntity) {
        check(tenantEntity);
        tenantMapper.persist(tenantEntity);
    }

    @Override
    public List<TenantEntity> findAll() {
        return tenantMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<TenantEntity> findPage(OkPageable page) {
        var paged = tenantMapper
                .findAll(Sort.descending("id"))
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());
    }

    @Override
    public TenantEntity get(Long id) {
        return tenantMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        tenantMapper.deleteById(id);
    }

    @Override
    public void delete(TenantEntity tenantEntity) {
        tenantMapper.delete(tenantEntity);
    }

    @Override
    public TenantEntity get(String uuid) {
        return tenantMapper.find("uuid", uuid).firstResult();
    }

    private static void check(TenantEntity tenant) {
        OkAssert.isTrue(OkStringUtil.isNotEmpty(tenant.getNo()), "no is empty");
        OkAssert.isTrue(OkStringUtil.isNotEmpty(tenant.getName()), "name is empty");
        OkAssert.isTrue(OkStringUtil.isNotEmpty(tenant.getUuid()), "uuid is empty");
        OkAssert.notNull(tenant.getStatus(), "status is null");
    }

    @Override
    public TenantEntity findByNo(String no) {
        return tenantMapper.findByNo(no);
    }

    @Override
    public void disable(Long id) {
        Log.infof("Disable tenant:%s", id);
        TenantEntity entity = tenantMapper.findById(id);
        entity.setDisabled(true);
    }

    @Override
    public void enable(Long id) {
        Log.infof("Enable tenant:%s", id);
        TenantEntity entity = tenantMapper.findById(id);
        entity.setDisabled(false);
    }
}
