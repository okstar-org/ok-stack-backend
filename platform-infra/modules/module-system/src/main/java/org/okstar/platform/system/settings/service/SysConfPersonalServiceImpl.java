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

package org.okstar.platform.system.settings.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.settings.SysConfDefines;
import org.okstar.platform.system.settings.domain.SysConfPersonal;
import org.okstar.platform.system.settings.domain.SysProperty;
import org.okstar.platform.system.settings.mapper.SysSetKvMapper;

import java.util.List;

@Transactional
@ApplicationScoped
public class SysConfPersonalServiceImpl implements SysConfPersonalService {

    @Inject
    SysSetKvMapper kvMapper;

    @Override
    public synchronized SysConfPersonal findDefault(SysAccount account) {
        SysConfPersonal personal = new SysConfPersonal();
        var property = kvMapper.findByGroupDomain(SysConfDefines.CONF_GROUP_PERSONAL_PREFIX, account.getUsername())
                .stream().findFirst();
        if (property.isPresent()) {
            SysProperty kv = property.get();
            personal.addProperty(kv);
        } else {
            personal.setLanguage(AccountDefines.DefaultLanguage);
            save(account, personal);
        }
        return personal;
    }

    @Override
    public SysProperty save(SysAccount account, SysConfPersonal personal) {
        SysProperty language;
        List<SysProperty> languages = kvMapper.findByKey(personal.getGroup(), account.getUsername(), "language");
        if (languages.isEmpty()) {
            language = new SysProperty();
        }else{
            language = languages.get(0);
        }
        language.setGrouping(SysConfDefines.CONF_GROUP_PERSONAL_PREFIX);
        language.setDomain(account.getUsername());
        language.setK("language");
        language.setV(personal.getLanguage());
        kvMapper.persist(language);
        return language;
    }

}
