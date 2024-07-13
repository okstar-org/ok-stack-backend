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

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.OrgDept;
import org.okstar.platform.org.mapper.OrgDeptMapper;
import org.okstar.platform.org.mapper.OrgPostMapper;

import java.util.List;

/**
 * 部门管理 服务实现
 */
@Transactional
@ApplicationScoped
public class OrgDeptServiceImpl implements OrgDeptService {
    @Inject
    OrgDeptMapper orgDeptMapper;
    @Inject
    OrgPostMapper orgPostMapper;


    @Override
    public void save(OrgDept dept) {
        orgDeptMapper.persist(dept);
    }

    @Override
    public List<OrgDept> findAll() {
        return orgDeptMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgDept> findPage(OkPageable pageable) {
        var all = orgDeptMapper.findAll();
        var query = all.page(Page.of(pageable.getPageIndex(), pageable.getPageSize()));
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
        var depts = children(id);
        OkAssert.isTrue(depts.isEmpty(), "存在下级，无法删除！");

        var query = orgPostMapper.find("deptId", id);
        OkAssert.isTrue(query.count()<1, "存在岗位，无法删除！");

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
    public List<OrgDept> getRootByOrgId(Long orgId) {
        return orgDeptMapper.list("parentId = ?1 and orgId = ?2", 0L, orgId).stream().toList();
    }

    @Override
    public long getCount() {
        return orgDeptMapper.count("disabled", false);
    }

    @Override
    public List<OrgDept> loadRootByOrgId(Long orgId) {
        List<OrgDept> depts = getRootByOrgId(orgId);
        if (!depts.isEmpty()) {
            return depts;
        }

        //初始化默认部门
        OrgDept root = new OrgDept();
        root.setOrgId(orgId);
        root.setParentId(0L);
        root.setLevel(0);
        root.setName("默认部门");
        root.setDisabled(false);
        save(root);
        return List.of(root);
    }


}
