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

package org.okstar.platform.system.account.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.OkJsonUtils;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.utils.OkAssert;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.jms.JmsTopicSender;
import org.okstar.platform.system.ModuleSystemApplication;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysAccountBind;
import org.okstar.platform.system.account.domain.SysProfile;
import org.okstar.platform.system.account.mapper.SysProfileMapper;

import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysProfileServiceImpl implements SysProfileService {

    @Inject
    SysProfileMapper mapper;
    @Inject
    SysAccountService accountService;
    @Inject
    OkJsonUtils jsonUtils;

    @Inject
    ConnectionFactory jmsFactory;
    @Inject
    JmsTopicSender topicSender;

    @Override
    public void save(SysProfile entity) {
        Log.infof("save: %s", entity);

        Long accountId = entity.getAccountId();
        OkAssert.notNull(accountId, "Account id not be null");

        String topic = ModuleSystemApplication.class.getSimpleName() + "." + SysProfile.class.getSimpleName() + ".UPDATE";

        SysProfile exist = null;
        if (entity.id != null && entity.id > 0) {
            exist = get(entity.id);
        }
        if (entity.getAccountId() != null && exist == null) {
            exist = loadByAccount(entity.getAccountId());
        }

        if (exist != null) {
            exist.setFirstName(entity.getFirstName());
            exist.setLastName(entity.getLastName());
            exist.setGender(entity.getGender());
            exist.setIdentify(entity.getIdentify());
            exist.setCountry(entity.getCountry());
            exist.setCity(entity.getCity());
            exist.setAddress(entity.getAddress());
            exist.setEmail(entity.getEmail());
            exist.setPhone(entity.getPhone());
            exist.setTelephone(entity.getTelephone());
            exist.setWebsite(entity.getWebsite());
            exist.setBirthday(entity.getBirthday());
            exist.setLanguage(entity.getLanguage());
            mapper.persist(exist);

            String msg = jsonUtils.asJsonString(exist);
            topicSender.send(topic, msg);
        } else {
            mapper.persist(entity);

            String msg = jsonUtils.asJsonString(entity);
            topicSender.send(topic, msg);
        }
        Log.infof("saved: %s", entity);
    }

    @Override
    public List<SysProfile> findAll() {
        return mapper.findAll().list();
    }

    @Override
    public OkPageResult<SysProfile> findPage(OkPageable page) {
        return null;
    }

    @Override
    public SysProfile get(Long id) {
        return mapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    @Override
    public void delete(SysProfile sysProfile) {
        mapper.delete(sysProfile);
    }

    @Override
    public SysProfile loadByUsername(String username) {
        SysAccount account = accountService.loadByUsername(username);
        OkAssert.notNull(account, "Invalid username");
        return getProfile(account);
    }

    @Override
    public SysProfile loadByAccount(Long accountId) {
        SysAccount account = accountService.get(accountId);
        OkAssert.notNull(account, "Invalid id");
        return getProfile(account);
    }

    private SysProfile getProfile(SysAccount account) {
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
