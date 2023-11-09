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

import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.mapper.OrgPostMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

/**
 * 岗位信息 服务层处理
 */
@ApplicationScoped
public class OrgPostServiceImpl implements OrgPostService {
    @Inject
    private OrgPostMapper orgPostMapper;

    @Override
    public void save(OrgPost orgPost) {
        orgPostMapper.persist(orgPost);
    }

    @Override
    public List<OrgPost> findAll() {
        return orgPostMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgPost> findPage(OkPageable page) {
        return null;
    }

    @Override
    public OrgPost get(Long id) {
        return orgPostMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        orgPostMapper.deleteById(id);
    }

    @Override
    public void delete(OrgPost orgPost) {
        orgPostMapper.delete(orgPost);
    }
}
