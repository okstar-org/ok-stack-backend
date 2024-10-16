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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.system.conf.domain.SysConfWebsite;
import org.okstar.platform.system.conf.domain.SysProperty;

import java.util.LinkedList;
import java.util.List;

@Transactional
@ApplicationScoped
public class SysConfSettingsServiceImpl implements SysConfSettingsService {

    @Inject
    SysPropertyService sysPropertyService;

    @Override
    public SysConfWebsite loadWebsite() {
        SysConfWebsite settings = new SysConfWebsite();
        List<SysProperty> list = sysPropertyService.findByGroup(settings.getGroup());
        list.forEach(p-> settings.addProperty(sysPropertyService.toDTO(p)));
        return settings;
    }

    @Override
    public List<SysProperty> saveWebsite(SysConfWebsite settings) {
        List<SysProperty> list = new LinkedList<>();
        settings.getProperties().forEach((k, dto) -> list.add(sysPropertyService.save(dto)));
        return list;
    }
}
