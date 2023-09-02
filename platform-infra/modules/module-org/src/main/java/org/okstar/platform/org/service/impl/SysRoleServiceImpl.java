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
import org.okstar.platform.org.domain.SysRole;
import org.okstar.platform.org.mapper.SysRoleMapper;
import org.okstar.platform.org.service.SysRoleService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * 角色 业务层处理
 */
@ApplicationScoped
public class SysRoleServiceImpl implements SysRoleService {
    @Inject
    private SysRoleMapper roleMapper;


    @Override
    public void save(SysRole sysRole) {
        roleMapper.persist(sysRole);
    }

    @Override
    public List<SysRole> findAll() {
        return null;
    }

    @Override
    public OkPageResult<SysRole> findPage(OkPageable pageable) {
        var all = roleMapper.findAll();
        var query = all.page(Page.of(pageable.getPageNumber(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public SysRole get(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(SysRole sysRole) {

    }
}
