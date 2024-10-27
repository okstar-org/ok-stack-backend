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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Topic;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.web.page.OkPageResult;
import org.okstar.platform.common.web.page.OkPageable;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.system.ModuleSystemApplication;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysAccountBind;
import org.okstar.platform.system.account.domain.SysProfile;
import org.okstar.platform.system.account.domain.SysProfile_;
import org.okstar.platform.system.account.mapper.SysProfileMapper;
import org.okstar.platform.system.conf.service.SysKeycloakService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysProfileServiceImpl implements SysProfileService {

    @Inject
    SysProfileMapper profileMapper;
    @Inject
    SysAccountService accountService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    SysKeycloakService keycloakService;

    @Inject
    ConnectionFactory connectionFactory;
    JMSContext context;

    static final String topicName = ModuleSystemApplication.class.getSimpleName()
            + "." + SysProfile.class.getSimpleName();

    public JMSContext ensureContext() {
        if (context == null) {
            context = connectionFactory.createContext();
        }
        return context;
    }

    public JMSProducer ensureProducer() {
        Log.infof("Ensure producer....");
        return ensureContext().createProducer();
    }

    public Topic ensureTopic() {
        Log.infof("Ensure topic....");
        return ensureContext().createTopic(topicName);
    }

    public void shutdown(@Observes ShutdownEvent e) {
        Log.infof("Shutdown....");
        if (context != null) {
            context.close();
        }
    }

    @Override
    public void update(SysAccount sysAccount, SysProfile sysProfile) {
        update(sysProfile, sysAccount.id);
        syncToKeycloak(sysAccount.getUsername());
    }

    @SneakyThrows(JsonProcessingException.class)
    @Override
    public void save(SysProfile entity) {
        Log.infof("save: %s", entity);

        Long id = entity.id;

        Long accountId = entity.getAccountId();
        OkAssert.notNull(accountId, "Account id not be null");
        profileMapper.persist(entity);
        Log.infof("saved profile id is: %s", entity.id);

        if (id != null) {
            ensureProducer().send(ensureTopic(), Map.of("UPDATED", objectMapper.writeValueAsString(entity)));
        } else {
            ensureProducer().send(ensureTopic(), Map.of("INSERTED", objectMapper.writeValueAsString(entity)));
        }
    }

    @Override
    public List<SysProfile> findAll() {
        return profileMapper.findAll().list();
    }

    @Override
    public OkPageResult<SysProfile> findPage(OkPageable page) {
        return null;
    }

    @Override
    public SysProfile get(Long id) {
        return profileMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        profileMapper.deleteById(id);
    }

    @Override
    public void delete(SysProfile sysProfile) {
        profileMapper.delete(sysProfile);
    }

    @Override
    public SysProfile get(String uuid) {
        return profileMapper.find("uuid", uuid).firstResult();
    }


    @Override
    public SysProfile loadByUsername(String username) {
        SysAccount account = accountService.loadByUsername(username);
        OkAssert.notNull(account, "Invalid username");
        return loadProfile(account.id);
    }

    @Override
    public SysProfile loadByAccount(Long accountId) {
        return loadProfile(accountId);
    }

    @Override
    public List<SysAccount> getByFirstName(String firstName) {
        List<SysProfile> list = profileMapper.find(SysProfile_.FIRST_NAME, firstName).stream().toList();
        return list.stream().map(e -> accountService.get(e.getAccountId())).toList();
    }

    @Override
    public List<SysAccount> getByLastName(String lastName) {
        List<SysProfile> list = profileMapper.find(SysProfile_.LAST_NAME, lastName).stream().toList();
        return list.stream().map(e -> accountService.get(e.getAccountId())).toList();
    }

    @Override
    public List<SysAccount> getByPersonalName(String personalName) {
        List<SysProfile> list = profileMapper.find(SysProfile_.FIRST_NAME + " || " + SysProfile_.LAST_NAME + " = ?1", personalName)
                .stream().toList();
        return list.stream().map(e -> accountService.get(e.getAccountId())).toList();
    }

    @Override
    public void syncToKeycloak(String username) {
        Optional<SysAccount> account = accountService.findByUsername(username);
        account.ifPresent(sysAccount -> {
            keycloakService.setUserProfile(sysAccount.getUid(), loadProfile(sysAccount.id));
        });
    }

    @Override
    public SysProfile loadProfile(Long accountId) {
        Optional<SysProfile> first = getProfile(accountId);
        if (first.isPresent()) {
            return first.get();
        }

        SysProfile profile = new SysProfile();
        profile.setAccountId(accountId);
        List<SysAccountBind> binds = accountService.listBind(accountId);
        binds.forEach(bind -> {
            if (bind.getBindType() == AccountDefines.BindType.email) {
                profile.setEmail(bind.getBindValue());
            } else if (bind.getBindType() == AccountDefines.BindType.phone) {
                profile.setPhone(bind.getBindValue());
            }
        });

        save(profile);
        return profile;
    }

    @Override
    public Optional<SysProfile> getProfile(Long accountId) {
        return profileMapper.find("accountId", accountId).stream().findFirst();
    }
}
