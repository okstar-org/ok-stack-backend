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

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.BooleanUtils;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.web.page.OkPageResult;
import org.okstar.platform.common.web.page.OkPageable;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.mapper.OrgMapper;

import java.util.List;
import java.util.UUID;

/**
 * 组织服务实现
 */
@Transactional
@ApplicationScoped
public class OrgServiceImpl implements OrgService {

    @Inject
    OrgMapper orgMapper;


    @Override
    public void save(Org org) {
        orgMapper.persist(org);
    }

    @Override
    public List<Org> findAll() {
        return orgMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<Org> findPage(OkPageable page) {
        var paged = orgMapper
                .findAll(Sort.descending("id"))
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());
    }

    @Override
    public Org get(Long id) {
        return orgMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        orgMapper.deleteById(id);
    }

    @Override
    public void delete(Org sysOrg) {
        orgMapper.delete(sysOrg);
    }

    @Override
    public Org get(String uuid) {
        return orgMapper.find("uuid", uuid).firstResult();
    }

    @Override
    public Org loadCurrent() {
        List<Org> all = findAll();
        if (all.isEmpty()) {
            return setDefault();
        }
        return all.stream().filter(o -> BooleanUtils.isTrue(o.getCurrent())).findFirst().get();
    }

    @Override
    public Org0 loadCurrent0() {
        Org org = loadCurrent();
        Org0 org0 = new Org0();
        OkBeanUtils.copyPropertiesTo(org, org0);
        return org0;
    }

    @Override
    public Org setDefault() {
        Org org = new Org();
        org.setCurrent(true);
        org.setName("");
        org.setUrl("");
        org.setNo(UUID.randomUUID().toString());
        org.setParentId(0L);
        create(org, 1L);
        return org;
    }

    @Override
    public void setCert(Long id, String cert) {
        Org org = get(id);
        org.setCert(cert);
    }

    @Override
    public Org save(Org0 org0) {
        OkAssert.isTrue(org0 != null && org0.getId() != null, "Invalid parameter!");
        Org org = get(org0.getId());
        org.setName(org0.getName());
        org.setUrl(org0.getUrl());
        org.setLocation(org0.getLocation());
        org.setAvatar(org0.getAvatar());
        return org;
    }
}
