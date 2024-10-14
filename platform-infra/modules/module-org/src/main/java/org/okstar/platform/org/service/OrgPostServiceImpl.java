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

package org.okstar.platform.org.service;

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.locationtech.jts.util.Assert;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.web.page.OkPageResult;
import org.okstar.platform.common.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.domain.OrgPost;
import org.okstar.platform.org.dto.OrgPost0;
import org.okstar.platform.org.mapper.OrgDeptMapper;
import org.okstar.platform.org.mapper.OrgPostMapper;

import java.util.List;
import java.util.Optional;

/**
 * 岗位信息 服务层处理
 */
@Transactional
@ApplicationScoped
public class OrgPostServiceImpl implements OrgPostService {
    @Inject
    OrgPostMapper postMapper;
    @Inject
    OrgDeptMapper deptMapper;

    @Override
    public void saveOrUpdate(OrgPost orgPost) {
        Log.debugf("save=%s", orgPost);

        if (orgPost.id != null && orgPost.id > 0) {
            //update
            OrgPost post = postMapper.findById(orgPost.id);
            OkAssert.notNull(post, "数据有误！");

            post.setName(orgPost.getName());
            post.setNo(orgPost.getNo());
            post.setDescr(orgPost.getDescr());
            post.setRecruit(orgPost.getRecruit());
            post.setUpdateAt(OkDateUtils.now());
            update(post, 1L);
            return;
        }

        //add
        Assert.isTrue(orgPost.getDeptId() != null && orgPost.getDeptId() > 0, "请选择部门");
        orgPost.setCreateAt(OkDateUtils.now());
        create(orgPost, 1L);
    }

    @Override
    public void save(OrgPost orgPost) {
        postMapper.persist(orgPost);
    }

    @Override
    public List<OrgPost> findAll() {
        return postMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgPost> findPage(OkPageable pageable) {
        var all = postMapper.findAll();
        var query = all.page(Page.of(pageable.getPageIndex(), pageable.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public OrgPost get(Long id) {
        return postMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        postMapper.deleteById(id);
    }

    @Override
    public void delete(OrgPost orgPost) {
        postMapper.delete(orgPost);
    }

    @Override
    public OrgPost get(String uuid) {
        return postMapper.find("uuid", uuid).firstResult();
    }

    @Override
    public List<OrgPost> findByDept(Long deptId) {
        return postMapper.find("deptId", deptId).stream().toList();
    }

    @Override
    public List<OrgPost0> findAssignAble(Boolean assignment, boolean disabled) {
        List<OrgPost> list = postMapper.find("disabled = ?1", disabled).list();
        return list.stream().map(p -> {
            OrgPost0 p0 = new OrgPost0();
            OkBeanUtils.copyPropertiesTo(p, p0);

            OrgDept dept = deptMapper.findById(p.getDeptId());
            if (dept != null) {
                p0.setDeptName(dept.getName());
            }

            return p0;
        }).toList();
    }

    @Override
    public long getCount() {
        return postMapper.count("disabled", false);
    }

    @Override
    public Optional<OrgPost> findByDeptAndName(Long departmentId, String name) {
        postMapper.find("deptId = ?1 and name = ?2", departmentId, name).firstResult();
        return Optional.empty();
    }
}
