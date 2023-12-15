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
import org.okstar.platform.common.core.utils.OkDateUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.mapper.OrgPostMapper;
import org.springframework.util.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

/**
 * 岗位信息 服务层处理
 */
@Transactional
@ApplicationScoped
public class OrgPostServiceImpl implements OrgPostService {
    @Inject
    OrgPostMapper orgPostMapper;

    @Override
    public void save(OrgPost orgPost) {
        if (orgPost.id != null && orgPost.id > 0) {
            OrgPost post = orgPostMapper.findById(orgPost.id);
            post.setName(orgPost.getName());
            post.setNo(orgPost.getNo());
            post.setDescr(orgPost.getDescr());
            post.setRecruit(orgPost.getRecruit());
            post.setUpdateAt(OkDateUtils.now());
            orgPostMapper.persist(post);
        } else {
            Assert.isTrue(orgPost.getDeptId() != null && orgPost.getDeptId() > 0, "请选择部门");
            orgPost.setCreateAt(OkDateUtils.now());
            orgPostMapper.persist(orgPost);
        }
    }

    @Override
    public List<OrgPost> findAll() {
        return orgPostMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgPost> findPage(OkPageable pageable) {
        var all = orgPostMapper.findAll();
        var query = all.page(Page.of(pageable.getPageNumber(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
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

    @Override
    public List<OrgPost> findByDept(Long deptId) {
        return orgPostMapper.find("deptId", deptId).stream().toList();
    }

    @Override
    public List<OrgPost> findAssignAble(Boolean assignment, boolean disabled) {
        return orgPostMapper.find((assignment ? " " : "assignFor is null and") + "  disabled = ?1", disabled).list();
    }
}
