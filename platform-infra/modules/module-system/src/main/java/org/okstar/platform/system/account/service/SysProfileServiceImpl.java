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

package org.okstar.platform.system.account.service;

import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysAccountBind;
import org.okstar.platform.system.account.domain.SysProfile;
import org.okstar.platform.system.account.mapper.SysProfileMapper;
import org.springframework.util.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysProfileServiceImpl implements SysProfileService {

    @Inject
    SysProfileMapper mapper;
    @Inject
    SysAccountService accountService;

    @Override
    public void save(SysProfile sysProfile) {
        mapper.persist(sysProfile);
    }

    @Override
    public List<SysProfile> findAll() {
        return null;
    }

    @Override
    public OkPageResult<SysProfile> findPage(OkPageable page) {
        return null;
    }

    @Override
    public SysProfile get(Long aLong) {
        return null;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(SysProfile sysProfile) {

    }

    @Override
    public SysProfile loadByAccountId(String username) {

        SysAccount account = accountService.loadByUsername(username);
        Assert.notNull(account, "Invalid username");

        Optional<SysProfile> first = mapper.find("accountId", account.id).stream().findFirst();
        if (first.isEmpty()) {
            SysProfile profile = new SysProfile();
            profile.setAccountId(account.id);

            List<SysAccountBind> binds = accountService.listBind(account.id);
            binds.forEach(bind -> {
                if (bind.getBindType() == AccountDefines.BindType.email) {
                    profile.setEmail(bind.getBindValue());
                } else if (bind.getBindType() == AccountDefines.BindType.phone) {
                    profile.setPhone(bind.getBindValue());
                }
            });
            mapper.persist(profile);
            return profile;
        }
        return first.get();

    }
}
