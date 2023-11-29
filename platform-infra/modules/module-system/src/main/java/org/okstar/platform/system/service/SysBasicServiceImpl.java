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

import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.system.domain.SysBasic;
import org.okstar.platform.system.mapper.SysBasicMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysBasicServiceImpl implements SysBasicService {

    @Inject
    SysBasicMapper mapper;


    @Override
    public void save(SysBasic basic) {
        SysBasic sysBasic = get(basic.id);
        sysBasic.setGlobalEnable(basic.isGlobalEnable());
        sysBasic.setVerifyAccount(basic.isVerifyAccount());
        sysBasic.setLocale(basic.getLocale());
        mapper.persist(sysBasic);
    }

    @Override
    public List<SysBasic> findAll() {
        return mapper.findAll().list();
    }

    @Override
    public OkPageResult<SysBasic> findPage(OkPageable page) {
        return null;
    }

    @Override
    public SysBasic get(Long id) {
        return mapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public void delete(SysBasic sysBasic) {
        mapper.delete(sysBasic);
    }

    @Override
    public synchronized SysBasic findDefault() {
        Optional<SysBasic> first = findAll().stream().findFirst();
        if (first.isPresent()) return first.get();
        SysBasic sysBasic = new SysBasic();
        sysBasic.setGlobalEnable(true);
        sysBasic.setVerifyAccount(false);
        sysBasic.setLocale(Locale.getDefault().toString());
        mapper.persist(sysBasic);
        return sysBasic;
    }
}
