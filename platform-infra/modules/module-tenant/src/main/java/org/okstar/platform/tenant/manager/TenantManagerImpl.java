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

package org.okstar.platform.tenant.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.Arc;
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
import jakarta.ws.rs.NotFoundException;
import lombok.SneakyThrows;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.common.os.PortFinder;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.tenant.ModuleTenantApplication;
import org.okstar.platform.tenant.defines.TenantDefined;
import org.okstar.platform.tenant.dto.TenantCreateDTO;
import org.okstar.platform.tenant.dto.TenantMetaDTO;
import org.okstar.platform.tenant.dto.TenantUpdateDTO;
import org.okstar.platform.tenant.entity.MetaEntity;
import org.okstar.platform.tenant.entity.TenantEntity;
import org.okstar.platform.tenant.os.DockerService;
import org.okstar.platform.tenant.service.MetaService;
import org.okstar.platform.tenant.service.TenantService;
import org.okstar.platform.tenant.utils.TenantUtil;

import java.util.concurrent.ExecutorService;

@Transactional
@ApplicationScoped
public class TenantManagerImpl implements TenantManager {

    @Inject
    TenantService tenantService;
    @Inject
    MetaService metaService;

    @Inject
    DockerService dockerService;
    @Inject
    ObjectMapper objectMapper;

    @Inject
    TenantUtil tenantUtil;

    @Inject
    ConnectionFactory connectionFactory;
    JMSContext context;
    JMSProducer producer;
    Topic topic;

    static final String topicName = ModuleTenantApplication.class.getSimpleName()
            + "." + TenantEntity.class.getSimpleName();

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


    @Override
    public Long updateTenant(TenantUpdateDTO updateDTO) {
        Log.infof("Update tenant: %s", updateDTO);
        OkAssert.notNull(updateDTO.getId(), "id is empty");

        TenantEntity entity = tenantService.get(updateDTO.getId());
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }

        entity.setName(updateDTO.getName());
        entity.setNo(updateDTO.getNo());
        entity.setUpdateAt(OkDateUtils.now());
        return entity.id;
    }

    @Override
    public void stop(Long tenantId) {
        TenantEntity entity = tenantService.get(tenantId);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }

        if (entity.getStatus() == TenantDefined.TenantStatus.Stopped) {
            Log.warnf("Tenant[%s] is already stopped!", tenantId);
            return;
        }

        MetaEntity meta = metaService.loadByTenant(entity.id);
        TenantMetaDTO metaDTO = tenantUtil.toMetaDTO(meta.getJsonValue());
        OkAssert.notNull(metaDTO, "没有资源元数据！");

        if (metaDTO.getRunOn() == TenantDefined.RunOn.docker) {
            TenantMetaDTO.DB db = metaDTO.getDb();
            stopContainer(db.getContainerId());
        }
        entity.setStatus(TenantDefined.TenantStatus.Stopped);

    }


    @Override
    public void start(Long tenantId) {
        TenantEntity entity = tenantService.get(tenantId);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }
        MetaEntity meta = metaService.loadByTenant(entity.id);
        TenantMetaDTO metaDTO = tenantUtil.toMetaDTO(meta.getJsonValue());
        OkAssert.notNull(metaDTO, "没有资源元数据！");

        if (metaDTO.getRunOn() == TenantDefined.RunOn.docker) {
            TenantMetaDTO.DB db = metaDTO.getDb();
            startContainer(db.getContainerId());
        }

        entity.setStatus(TenantDefined.TenantStatus.Started);
    }

    @Override
    public Long createTenant(TenantCreateDTO createDTO) {
        Log.infof("Create tenant: %s", createDTO);

        OkAssert.isTrue(OkStringUtil.isNotEmpty(createDTO.getNo()), "no is empty");

        TenantEntity exist = tenantService.findByNo(createDTO.getNo());
        OkAssert.isTrue(exist == null, "已存在同编号租户！");

        //保存记录
        TenantEntity tenantEntity = new TenantEntity();
        tenantEntity.setNo(createDTO.getNo());
        tenantEntity.setName(createDTO.getName());
        tenantEntity.setStatus(TenantDefined.TenantStatus.Created);
        tenantEntity.setUuid(OkIdUtils.makeUuid());
        tenantEntity.setCreateAt(OkDateUtils.now());
        tenantEntity.setDisabled(false);
        tenantService.save(tenantEntity);

        //初始化租户环境
        ExecutorService executorService = Arc.container().getExecutorService();
        executorService.execute(() -> {
            createPgSql(tenantEntity.id);
        });

        return tenantEntity.id;
    }

    @SneakyThrows
    @Override
    public void createPgSql(Long tenantId) {
        Log.infof("Create PgSql tenant: %s", tenantId);
        String image = "postgres:latest";
        String name = "tenant_%s_%s".formatted(tenantId, image.split(":")[0]);

        String existContainerId = dockerService.findContainerByName(name);
        Log.infof("Find exist containerId=>%s", existContainerId);
        OkAssert.isTrue(existContainerId == null, "资源已存在！");

        String password = "ok123456";
        String username = "okstar";
        int localPort = PortFinder.findAvailablePort();
        String[] env = {"POSTGRES_PASSWORD=" + password, "POSTGRES_USER=" + username};

        String containerId = dockerService.createContainer(name, image, "2345/tcp", localPort + ":2345", env);
        Log.infof("create existContainerId[%s]=> %s for tenant: %s", image, containerId, tenantId);


        MetaEntity metaEntity = metaService.loadByTenant(tenantId);

        TenantMetaDTO metaDTO = new TenantMetaDTO();
        metaDTO.setDbType(TenantDefined.DataBaseType.pgsql);
        metaDTO.setRunOn(TenantDefined.RunOn.docker);

        var db = new TenantMetaDTO.DB();
        db.setPassword(password);
        db.setUsername(username);
        db.setJdbcUrl("jdbc:postgresql://localhost:" + localPort);
        db.setContainerId(containerId);
        metaDTO.setDb(db);
        metaEntity.setJsonValue(objectMapper.writeValueAsString(metaDTO));
        metaService.save(metaEntity);

        Log.infof("Create PgSql container successfully tenant: %s", tenantId);
    }

    @Override
    public void startContainer(String containerId) {
        Log.infof("Start container: %s", containerId);
        dockerService.startContainer(containerId);
    }

    private void stopContainer(String containerId) {
        Log.infof("Stop container: %s", containerId);
        dockerService.stopContainer(containerId);
    }
}
