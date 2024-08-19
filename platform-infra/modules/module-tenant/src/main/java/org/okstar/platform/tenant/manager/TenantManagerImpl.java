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

import io.quarkus.arc.Arc;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.SneakyThrows;
import org.okstar.platform.common.asserts.OkAssert;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.core.DataSourceDefines;
import org.okstar.platform.common.core.DatabaseResource;
import org.okstar.platform.common.core.RunOn;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.common.os.PortFinder;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.system.vo.SysAccount0;
import org.okstar.platform.tenant.defines.TenantDefined;
import org.okstar.platform.tenant.doc.TenantMetaDoc;
import org.okstar.platform.tenant.dto.TenantCreateDTO;
import org.okstar.platform.tenant.dto.TenantDetailDTO;
import org.okstar.platform.tenant.dto.TenantUpdateDTO;
import org.okstar.platform.tenant.entity.TenantEntity;
import org.okstar.platform.tenant.os.DockerService;
import org.okstar.platform.tenant.repo.MetaDocMapper;
import org.okstar.platform.tenant.service.TenantService;

import java.util.concurrent.ExecutorService;

@Transactional
@ApplicationScoped
public class TenantManagerImpl implements TenantManager {

    @Inject
    TenantService tenantService;

    @Inject
    DockerService dockerService;

    @Inject
    MetaDocMapper metaDocMapper;


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

        TenantMetaDoc metaDTO = metaDocMapper.getMetaDoc(entity.id);
        OkAssert.notNull(metaDTO, "没有资源元数据！");

        for (var db : metaDTO.getDbs()) {
            stopContainer(db.getContainerId());
        }
        entity.setStatus(TenantDefined.TenantStatus.Stopped);
    }

    @Override
    public TenantDetailDTO loadDetail(Long tenantId) {
        TenantDetailDTO detailDTO = new TenantDetailDTO();

        TenantEntity entity = tenantService.get(tenantId);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }
        OkBeanUtils.copyPropertiesTo(entity, detailDTO);

        TenantMetaDoc metaDTO = metaDocMapper.getMetaDoc(entity.id);
        detailDTO.setMeta(metaDTO);

        return detailDTO;
    }


    @Override
    public void start(Long tenantId) {
        TenantEntity entity = tenantService.get(tenantId);
        if (entity == null) {
            throw new NotFoundException("不存在租户！");
        }

        TenantMetaDoc metaDTO = metaDocMapper.getMetaDoc(entity.id);
        OkAssert.notNull(metaDTO, "没有资源元数据！");
        for (var db : metaDTO.getDbs()) {
            startContainer(db.getContainerId());
        }

        entity.setStatus(TenantDefined.TenantStatus.Started);
    }


    @Override
    public Long create(TenantCreateDTO createDTO, SysAccount0 self) {
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
        tenantService.create(tenantEntity, self.getId());

        //初始化租户环境
        ExecutorService executorService = Arc.container().getExecutorService();
        executorService.execute(() -> {
            createResource(tenantEntity.id);
        });

        return tenantEntity.id;
    }

    @SneakyThrows
    @Override
    public void createResource(Long tenantId) {
        Log.infof("Create resource tenant: %s", tenantId);

        TenantMetaDoc metaDTO = metaDocMapper.loadMetaDoc(tenantId);
        createMongo(tenantId, metaDTO);
        createRedis(tenantId, metaDTO);
        Long count = metaDocMapper.update(metaDTO);
        Log.infof("Inserted tenant meta => %s", count);
        Log.infof("Create resource successfully tenant: %s", tenantId);
    }

    private void createRedis(Long tenantId, TenantMetaDoc metaDTO) {
        //redis

        String image = "redis";
        String name = "tenant_%s_%s".formatted(tenantId, image);
        String existContainerId = dockerService.findContainerByName(name);
        Log.infof("Find exist containerId=>%s", existContainerId);
        OkAssert.isTrue(existContainerId == null, "资源已存在！");

        String[] env = {};
        int localPort = PortFinder.findAvailablePort();
        String redisContainerId = dockerService.createContainer(name, image, "6379/tcp", localPort + ":6379", env);
        Log.infof("create redisContainerId[%s]=> %s for tenant: %s", image, redisContainerId, tenantId);

        var pgDb = new DatabaseResource();
        pgDb.setUrl("redis://localhost:" + localPort);
        pgDb.setRunOn(RunOn.docker);
        pgDb.setContainerId(redisContainerId);
        pgDb.setDbType(DataSourceDefines.DatabaseType.redis);
        metaDTO.addDb(pgDb);
    }


    /**
     * Create mongo container
     * @param tenantId tenant id
     * @param metaDTO
     */
    private void createMongo(Long tenantId, TenantMetaDoc metaDTO) {
        //mongo docker pull quay.kubesre.xyz/mongodb/mongodb-community-server:latest
        String image = "mongodb/mongodb-community-server";
        String name = "tenant_%s_%s".formatted(tenantId, image.split("/")[1]);
        String existContainerId = dockerService.findContainerByName(name);
        Log.infof("Find exist containerId=>%s", existContainerId);
        OkAssert.isTrue(existContainerId == null, "资源已存在！");

        String[] env = {"--replSet=rs0"};
        int localPort = PortFinder.findAvailablePort();
        String mongoContainerId = dockerService.createContainer(name, image, "27017/tcp", localPort + ":27017", env);
        Log.infof("create mongoContainerId[%s]=> %s for tenant: %s", image, mongoContainerId, tenantId);

        var pgDb = new DatabaseResource();
        pgDb.setUrl("mongodb://localhost:" + localPort);
        pgDb.setRunOn(RunOn.docker);
        pgDb.setContainerId(mongoContainerId);
        pgDb.setDbType(DataSourceDefines.DatabaseType.mongo);
        metaDTO.addDb(pgDb);
    }

    private void createPgSql(Long tenantId, TenantMetaDoc metaDTO) {
        //pgsql
        String image = "postgres:latest";
        String name = "tenant_%s_%s".formatted(tenantId, image.split(":")[0]);
        String existContainerId = dockerService.findContainerByName(name);
        Log.infof("Find exist containerId=>%s", existContainerId);
        OkAssert.isTrue(existContainerId == null, "资源已存在！");

        String username = "okstar";
        String password = "ok123456";
        String[] env = {"POSTGRES_PASSWORD=" + password, "POSTGRES_USER=" + username};
        int localPort = PortFinder.findAvailablePort();
        String pgContainerId = dockerService.createContainer(name, image, "2345/tcp", localPort + ":2345", env);
        Log.infof("create pgContainerId[%s]=> %s for tenant: %s", image, pgContainerId, tenantId);

        var pgDb = new DatabaseResource();
        pgDb.setPassword(password);
        pgDb.setUsername(username);
        pgDb.setUrl("jdbc:postgresql://localhost:" + localPort);
        pgDb.setContainerId(pgContainerId);
        pgDb.setRunOn(RunOn.docker);
        pgDb.setDbType(DataSourceDefines.DatabaseType.pgsql);
        metaDTO.addDb(pgDb);
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
