/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
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

package org.okstar.platform.org.service;

import io.quarkus.panache.common.Page;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.mapper.OrgDeptMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * 部门管理 服务实现
 */
@ApplicationScoped
public class OrgDeptServiceImpl implements OrgDeptService {
    @Inject
    private OrgDeptMapper orgDeptMapper;

    @Override
    public void save(OrgDept sysDept) {
        orgDeptMapper.persist(sysDept);
    }

    @Override
    public List<OrgDept> findAll() {
        return orgDeptMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgDept> findPage(OkPageable pageable) {
        var all = orgDeptMapper.findAll();
        var query = all.page(Page.of(pageable.getPageNumber(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public OrgDept get(Long id) {
        return orgDeptMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        orgDeptMapper.deleteById(id);
    }

    @Override
    public void delete(OrgDept sysDept) {
        orgDeptMapper.delete(sysDept);
    }

    @Override
    public List<OrgDept> children(Long parentId) {
        return orgDeptMapper.list("parentId", parentId).stream().toList();
    }
    @Override
    public List<OrgDept> getByOrgId(Long orgId) {
        return orgDeptMapper.list("parentId = ?1 and orgId = ?2", 0L, orgId).stream().toList();
    }


}
