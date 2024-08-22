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

package org.okstar.platform.tenant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.tenant.dto.InstanceDTO;
import org.okstar.platform.tenant.dto.InstanceRunningDTO;
import org.okstar.platform.tenant.entity.InstanceEntity;
import org.okstar.platform.tenant.repo.InstanceMapper;

import java.util.List;

@Transactional
@ApplicationScoped
public class InstanceServiceImpl implements InstanceService {
    @Inject
    InstanceMapper instanceMapper;
    @Inject
    ObjectMapper objectMapper;


    @Override
    public void save(InstanceEntity metaEntity) {
        instanceMapper.persist(metaEntity);
    }

    @Override
    public List<InstanceEntity> findAll() {
        return instanceMapper.findAll().stream().toList();
    }

    @Override
    public OkPageResult<InstanceEntity> findPage(OkPageable page) {
        var paged = instanceMapper
                .findAll(Sort.descending("id"))
                .page(page.getPageIndex(), page.getPageSize());
        return OkPageResult.build(paged.list(), paged.count(), paged.pageCount());
    }

    @Override
    public OkPageResult<InstanceDTO> findPageDTO(OkPageable page) {

        var paged = instanceMapper
                .findAll(Sort.descending("id"))
                .page(page.getPageIndex(), page.getPageSize());

        List<InstanceDTO> list = paged.list().stream().map(e -> {
            InstanceDTO dto = InstanceDTO.builder()
                    .id(e.id)
                    .no(e.getNo())
                    .uuid(e.getUuid())
                    .name(e.getName())
                    .status(e.getStatus())
                    .appId(e.getAppId())
                    .description(e.getDescription())
                    .createAt(e.getCreateAt())
                    .updateAt(e.getUpdateAt())
                    .build();

            var r = e.getRunning();
            if (r != null) {
                try {
                    InstanceRunningDTO runningDTO = objectMapper.readValue(r, InstanceRunningDTO.class);
                    if (runningDTO.getPorts() != null) {
                        for (String port : runningDTO.getPorts()) {
                            String localPort = port.split(":")[0];
                            if (port.endsWith("80")) {
                                dto.addUrl("http://localhost:%s".formatted(localPort));
                            } else if (port.endsWith("443")) {
                                dto.addUrl("https://localhost:%s".formatted(localPort));
                            }
                        }
                    }
                    if (runningDTO.getVolumes() != null) {
                        for (String v : runningDTO.getVolumes()) {
                            if (OkStringUtil.isNoneEmpty(v)) {
                                dto.addVolumes(v.split(":")[0]);
                            }
                        }
                    }
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
            }

            return dto;
        }).toList();

        return OkPageResult.build(list, paged.count(), paged.pageCount());
    }


    @Override
    public InstanceEntity get(Long id) {
        return instanceMapper.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        instanceMapper.deleteById(id);
    }

    @Override
    public void delete(InstanceEntity metaEntity) {
        instanceMapper.delete(metaEntity);
    }

    @Override
    public InstanceEntity get(String uuid) {
        return instanceMapper.find("uuid", uuid).firstResult();
    }

}
