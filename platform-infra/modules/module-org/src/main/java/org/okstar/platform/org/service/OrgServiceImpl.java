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

import org.apache.commons.lang3.BooleanUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.mapper.OrgMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class OrgServiceImpl implements OrgService {
    @Inject
    OrgMapper orgMapper;

    @Override
    public void save(Org sysOrg) {

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
    public Org get(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Org sysOrg) {

    }

    @Override
    public Optional<Org> current() {
        List<Org> orgs = findAll();
        return orgs.stream().filter(o -> BooleanUtils.isTrue( o.getCurrent())).findFirst();
    }
}
