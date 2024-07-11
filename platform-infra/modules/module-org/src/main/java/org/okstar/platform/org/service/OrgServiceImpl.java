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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.BooleanUtils;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.utils.OkDateUtils;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.mapper.OrgMapper;

import java.util.List;
import java.util.UUID;

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
        return null;
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
    public Org current() {
        List<Org> all = findAll();
        if (all.isEmpty()) {
            return setDefault();
        }
        return all.stream().filter(o -> BooleanUtils.isTrue(o.getCurrent())).findFirst().get();
    }

    @Override
    public Org0 current0() {
        Org org = current();
        Org0 org0 = new Org0();
        OkBeanUtils.copyPropertiesTo(org, org0);
        return org0;
    }

    @Override
    public Org setDefault() {
        Org org = new Org();
        org.setCurrent(true);
        org.setName("unnamed");
        org.setUrl("example.org");
        org.setNo(UUID.randomUUID().toString());
        org.setParentId(0L);
        org.setCreateBy(0L);
        org.setCreateAt(OkDateUtils.now());
        save(org);
        return org;
    }

    @Override
    public void setCert(Long id, String cert) {
        Org org = get(id);
        org.setCert(cert);
    }

    @Override
    public Boolean save(Org0 org0) {
        OkAssert.isTrue(org0 != null && org0.getId() != null, "Invalid parameter!");
        Org org = get(org0.getId());
        org.setName(org0.getName());
        org.setUrl(org0.getUrl());
        org.setLocation(org0.getLocation());
        org.setAvatar(org0.getAvatar());
        return true;
    }
}
