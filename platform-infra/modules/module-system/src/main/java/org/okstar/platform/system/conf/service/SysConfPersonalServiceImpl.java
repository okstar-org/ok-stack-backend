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
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.conf.SysConfDefines;
import org.okstar.platform.system.conf.domain.SysConfPersonal;
import org.okstar.platform.system.conf.domain.SysProperty;

import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysConfPersonalServiceImpl implements SysConfPersonalService {

    @Inject
    SysPropertyService sysPropertyService;

    @Override
    public synchronized SysConfPersonal findDefault(SysAccount account) {
        SysConfPersonal personal = new SysConfPersonal();
        List<SysProperty> list = sysPropertyService.findByGroupDomain(personal.getGroup(), account.getUsername());
        Optional<SysProperty> property = list.stream().findFirst();
        if (property.isPresent()) {
            personal.addProperty(sysPropertyService.toDTO(property.get()));
        } else {
            personal.setLanguage(AccountDefines.DefaultLanguage);
            save(account, personal);
        }
        return personal;
    }

    @Override
    public List<SysProperty> save(SysAccount account, SysConfPersonal personal) {
        return personal.getProperties().stream()//
                .map(property -> {
                    property.setDomain(account.getUsername());
                    return sysPropertyService.save(property);
                }).toList();
    }


    @Override
    public SysProperty saveLanguage(SysAccount account, String lang) {
        Optional<SysProperty> language = sysPropertyService.findByKey(SysConfDefines.CONF_GROUP_PERSONAL_PREFIX, account.getUsername(), "language");
        language.ifPresent(sysProperty -> {
            sysProperty.setV(lang);
            sysProperty.setDomain(account.getUsername());
            sysPropertyService.update(sysProperty, 1L);
        });
        return language.orElse(null);
    }

}
