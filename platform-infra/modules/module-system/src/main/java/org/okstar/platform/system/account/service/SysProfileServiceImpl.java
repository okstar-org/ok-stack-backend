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
import io.quarkus.runtime.StartupEvent;
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
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.system.ModuleSystemApplication;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysAccountBind;
import org.okstar.platform.system.account.domain.SysProfile;
import org.okstar.platform.system.account.domain.SysProfile_;
import org.okstar.platform.system.account.mapper.SysProfileMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysProfileServiceImpl implements SysProfileService {

    @Inject
    SysProfileMapper mapper;
    @Inject
    SysAccountService accountService;
    @Inject
    ObjectMapper objectMapper;

    @Inject
    ConnectionFactory connectionFactory;

    JMSContext context;
    JMSProducer producer;
    Topic topic;

    static final String topicName = ModuleSystemApplication.class.getSimpleName()
            + "." + SysProfile.class.getSimpleName();
    private Map map;

    public void startup(@Observes StartupEvent e) {
        Log.infof("Initialize....");
        context = connectionFactory.createContext();
        producer = context.createProducer();
        topic = context.createTopic(topicName);
    }

    public void shutdown(@Observes ShutdownEvent e) {
        Log.infof("Shutdown....");
        context.close();
    }

    @SneakyThrows(JsonProcessingException.class)
    @Override
    public void save(SysProfile entity) {
        Log.infof("save: %s", entity);

        Long accountId = entity.getAccountId();
        OkAssert.notNull(accountId, "Account id not be null");

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
            producer.send(topic, Map.of("UPDATED", objectMapper.writeValueAsString(exist)));
        } else {
            mapper.persist(entity);
            producer.send(topic, Map.of("INSERTED", objectMapper.writeValueAsString(exist)));
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
    public SysProfile get(String uuid) {
        return mapper.find("uuid", uuid).firstResult();
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

    @Override
    public List<SysAccount> loadByFirstName(String firstName) {
        List<SysProfile> list = mapper.find(SysProfile_.FIRST_NAME, firstName).stream().toList();
        return list.stream().map(e -> accountService.get(e.getAccountId())).toList();
    }

    @Override
    public List<SysAccount> loadByLastName(String lastName) {
        List<SysProfile> list = mapper.find(SysProfile_.LAST_NAME, lastName).stream().toList();
        return list.stream().map(e -> accountService.get(e.getAccountId())).toList();
    }

    @Override
    public List<SysAccount> loadByPersonalName(String personalName) {
        List<SysProfile> list = mapper.find( SysProfile_.FIRST_NAME+" || "+ SysProfile_.LAST_NAME, personalName)
                .stream().toList();
        return list.stream().map(e -> accountService.get(e.getAccountId())).toList();
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
