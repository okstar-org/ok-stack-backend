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

package org.okstar.platform.system.conf.service;

import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.web.page.OkPageResult;
import org.okstar.platform.common.web.page.OkPageable;
import org.okstar.platform.system.dto.SysPropertyDTO;
import org.okstar.platform.system.conf.domain.SysProperty;
import org.okstar.platform.system.conf.domain.SysProperty_;
import org.okstar.platform.system.conf.mapper.SysPropertyMapper;

import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysPropertyServiceImpl implements SysPropertyService {

    @Inject
    SysPropertyMapper sysPropertyMapper;

    @Override
    public void save(SysProperty property) {
        sysPropertyMapper.persist(property);
    }

    @Override
    public List<SysProperty> findAll() {
        return sysPropertyMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<SysProperty> findPage(OkPageable page) {
        var all = sysPropertyMapper.findAll();
        var query = all.page(Page.of(page.getPageIndex(), page.getPageSize()));
        return OkPageResult.build(
                query.list(),
                query.count(),
                query.pageCount());
    }

    @Override
    public SysProperty get(Long id) {
        return sysPropertyMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        sysPropertyMapper.deleteById(id);
    }

    @Override
    public void delete(SysProperty property) {
        sysPropertyMapper.delete(property);
    }

    @Override
    public SysProperty get(String uuid) {
        return sysPropertyMapper.find(SysProperty_.UUID, uuid).firstResult();
    }

    @Override
    public void deleteByGroup(String group) {
        sysPropertyMapper.deleteByGroup(group);
    }

    @Override
    public List<SysProperty> findByGroup(String group) {
        return sysPropertyMapper.findByGroup(group);
    }

    @Override
    public List<SysProperty> findByGroupDomain(String group, String domain) {
        return sysPropertyMapper.findByGroupDomain(group, domain);
    }

    @Override
    public Optional<SysProperty> findByKey(String group, String k) {
        return sysPropertyMapper.findByKey(group, k);
    }

    @Override
    public Optional<SysProperty> findByKey(String group, String domain, String k) {
        return sysPropertyMapper.findByKey(group, domain, k);
    }

    @Override
    public SysPropertyDTO toDTO(SysProperty e) {
        var d = new SysPropertyDTO();
        OkBeanUtils.copyPropertiesTo(e, d);
        return d;
    }

    @Override
    public List<SysPropertyDTO> toDTOs(List<SysProperty> ps) {
        return ps.stream().map(this::toDTO).toList();
    }

    @Override
    public SysProperty save(SysPropertyDTO dto) {
        Optional<SysProperty> optional = dto.getDomain() == null ?
                findByKey(dto.getGrouping(), dto.getK()) :
                findByKey(dto.getGrouping(), dto.getDomain(), dto.getK());

        if (optional.isEmpty()) {
            //create
            SysProperty t = new SysProperty();
            OkBeanUtils.copyPropertiesTo(dto, t);
            create(t, 1L);
            return t;
        } else {
            //update
            SysProperty property = optional.get();
            property.setV(dto.getV());
            update(property, 1L);
            return property;
        }
    }
}
