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

package org.okstar.platform.org.service.impl;

import io.quarkus.panache.common.Page;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.SysOrgDept;
import org.okstar.platform.org.mapper.SysDeptMapper;
import org.okstar.platform.org.service.ISysDeptService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * 部门管理 服务实现
 */
@ApplicationScoped
public class SysDeptServiceImpl implements ISysDeptService {
    @Inject
    private SysDeptMapper deptMapper;

    @Override
    public void save(SysOrgDept sysDept) {
        deptMapper.persist(sysDept);
    }

    @Override
    public List<SysOrgDept> findAll() {
        return deptMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<SysOrgDept> findPage(OkPageable pageable) {
        var all = deptMapper.findAll();
        var query = all.page(Page.of(pageable.getPageNumber(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public SysOrgDept get(Long id) {
        return deptMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        deptMapper.deleteById(id);
    }

    @Override
    public void delete(SysOrgDept sysDept) {
        deptMapper.delete(sysDept);
    }
}
