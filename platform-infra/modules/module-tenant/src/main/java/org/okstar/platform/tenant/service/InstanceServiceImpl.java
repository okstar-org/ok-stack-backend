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
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.billing.rpc.BillingOrderRpc;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.tenant.entity.InstanceEntity;
import org.okstar.platform.tenant.repo.InstanceMapper;

import java.util.List;

@Transactional
@ApplicationScoped
public class InstanceServiceImpl implements InstanceService {
    @Inject
    InstanceMapper instanceMapper;


    @Override
    public void save(InstanceEntity metaEntity) {
        instanceMapper.persist(metaEntity);
    }

    @Override
    public List<InstanceEntity> findAll() {
        return instanceMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<InstanceEntity> findPage(OkPageable page) {
        var paged = instanceMapper
                .findAll(Sort.descending("id"))
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());
    }

    @Override
    public InstanceEntity get(Long id) {
        return instanceMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        instanceMapper.deleteById(id);
    }

    @Override
    public void delete(InstanceEntity metaEntity) {
        instanceMapper.delete(metaEntity);
    }

}
