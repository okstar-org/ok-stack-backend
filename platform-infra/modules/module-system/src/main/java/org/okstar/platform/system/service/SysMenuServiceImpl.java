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

package org.okstar.platform.system.service;


import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.system.domain.SysMenu;
import org.okstar.platform.system.mapper.SysMenuMapper;

import java.util.List;


/**
 * 菜单 业务层处理
 */
@ApplicationScoped
public class SysMenuServiceImpl implements SysMenuService {

//    @Inject
    SysMenuMapper sysMenuMapper;

    @Override
    public void save(SysMenu sysMenu) {
        sysMenuMapper.persist(sysMenu);
    }

    @Override
    public List<SysMenu> findAll() {
        return sysMenuMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<SysMenu> findPage(OkPageable pageable) {
        var all = sysMenuMapper.findAll();
        var query = all.page(Page.of(pageable.getPageIndex(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public SysMenu get(Long id) {
        return sysMenuMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        sysMenuMapper.deleteById(id);
    }

    @Override
    public void delete(SysMenu sysMenu) {
        sysMenuMapper.delete(sysMenu);
    }
}
