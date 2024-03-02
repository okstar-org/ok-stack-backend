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
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.settings.domain.SysSetGlobal;
import org.okstar.platform.system.settings.domain.SysSetPersonal;
import org.okstar.platform.system.settings.mapper.SysSetGlobalMapper;
import org.okstar.platform.system.settings.mapper.SysSetPersonalMapper;

import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysBasicServiceImpl implements SysBasicService {

    @Inject
    SysSetGlobalMapper globalMapper;
    @Inject
    SysSetPersonalMapper personalMapper;


    @Override
    public synchronized void save(SysSetGlobal setGlobal) {
        OkAssert.notNull(setGlobal, "id is null");

        if (OkStringUtil.isBlank(setGlobal.getXmppHost())) {
            setGlobal.setXmppHost(null);
        }

        if (setGlobal.getXmppHost() != null) {
            OkAssert.isTrue(OkStringUtil.isValidHostAddr(setGlobal.getXmppHost(), false), "IM服务器地址格式不正确！");
        }

        SysSetGlobal global = get(setGlobal.id);
        global.setGlobalEnable(setGlobal.isGlobalEnable());
        global.setVerifyAccount(setGlobal.isVerifyAccount());
        global.setXmppHost(setGlobal.getXmppHost());
        globalMapper.persist(global);
    }

    @Override
    public List<SysSetGlobal> findAll() {
        return globalMapper.findAll().list();
    }

    @Override
    public OkPageResult<SysSetGlobal> findPage(OkPageable page) {
        return null;
    }

    @Override
    public SysSetGlobal get(Long id) {
        return globalMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        globalMapper.deleteById(id);
    }

    @Override
    public void delete(SysSetGlobal sysBasic) {
        globalMapper.delete(sysBasic);
    }

    @Override
    public synchronized SysSetGlobal findDefaultGlobal() {
        Optional<SysSetGlobal> first = findAll().stream().findFirst();
        if (first.isPresent()) return first.get();

        SysSetGlobal global = new SysSetGlobal();
        global.setGlobalEnable(true);
        global.setVerifyAccount(false);
        globalMapper.persist(global);
        return global;
    }

    @Override
    public synchronized SysSetPersonal findDefaultPersonal(SysAccount account) {
        Optional<SysSetPersonal> first = personalMapper.find("accountId", account.id)
                .stream().findFirst();
        if (first.isPresent()) return first.get();

        SysSetPersonal personal = new SysSetPersonal();
        personal.setAccountId(account.id);
        personal.setLanguage(AccountDefines.DefaultLanguage);
        personalMapper.persist(personal);
        return personal;
    }

    @Override
    public synchronized void savePersonal(SysSetPersonal personal) {
        SysSetPersonal exist = getPersonal(personal.id);
        exist.setLanguage(personal.getLanguage());
        personalMapper.persist(exist);
    }

    private SysSetPersonal getPersonal(Long id) {
        return personalMapper.findById(id);
    }
}
