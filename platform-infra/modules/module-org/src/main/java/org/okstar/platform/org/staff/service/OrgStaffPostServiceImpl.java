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

package org.okstar.platform.org.staff.service;

import io.quarkus.panache.common.Page;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgStaffPost;
import org.okstar.platform.org.mapper.OrgStaffPostMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;


/**
 * 人员服务
 */
@ApplicationScoped
public class OrgStaffPostServiceImpl implements OrgStaffPostService {

    @Inject
    OrgStaffPostMapper orgStaffPostMapper;



    @Override
    public void save(OrgStaffPost sysDept) {
        orgStaffPostMapper.persist(sysDept);
    }

    @Override
    public List<OrgStaffPost> findAll() {
        return orgStaffPostMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgStaffPost> findPage(OkPageable pageable) {
        var all = orgStaffPostMapper.findAll();
        var query = all.page(Page.of(pageable.getPageNumber(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public OrgStaffPost get(Long id) {
        return orgStaffPostMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        orgStaffPostMapper.deleteById(id);
    }

    @Override
    public void delete(OrgStaffPost sysDept) {
        orgStaffPostMapper.delete(sysDept);
    }

}
