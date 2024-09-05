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

package org.okstar.platform.work.rpc.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.cloud.entity.AppEntities;
import org.okstar.cloud.entity.AppMetaEntity;
import org.okstar.cloud.entity.OkPageable;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.work.dto.AppDTO;
import org.okstar.platform.work.dto.AppMetaDTO;
import org.okstar.platform.work.dto.RunModality;
import org.okstar.platform.work.rpc.WorkAppRpc;
import org.okstar.platform.work.service.WorkAppService;

import java.util.List;

@ApplicationScoped
public class WorkAppRpcImpl implements WorkAppRpc {

    @Inject
    WorkAppService workAppService;

    @Override
    public RpcResult<List<AppDTO>> list() {
        AppEntities entities = workAppService.page(OkPageable.builder().pageIndex(0).pageSize(100).build());
        List<AppDTO> list = entities.getList().stream().map(e -> AppDTO.builder()
                .uuid(e.getUuid()).no(e.getNo())
                .name(e.getName()).build()).toList();
        return RpcResult.success(list);
    }

    @Override
    public RpcResult<AppMetaDTO> meta(String uuid) {
        AppMetaEntity meta = workAppService.getMeta(uuid);
        return RpcResult.success(toDTO(meta));
    }

    private AppMetaDTO toDTO(AppMetaEntity meta) {
        return AppMetaDTO.builder()
                .appUuid(meta.getAppUuid())
                .uuid(meta.getUuid())
                .runOn(meta.getRunOn())
                .runModality(RunModality.valueOf(meta.getRunModality().toString()))
                .build();
    }
}
