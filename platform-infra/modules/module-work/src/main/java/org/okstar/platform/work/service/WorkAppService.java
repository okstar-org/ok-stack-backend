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

package org.okstar.platform.work.service;

import org.okstar.cloud.entity.*;

/**
 * 应用服务接口
 */
public interface WorkAppService {
    /**
     * 查询应用分页
     * @param pageable 分页
     * @return AppEntities
     */
    AppEntities page(OkPageable pageable);

    /**
     * 获取指定APP
     * @param uuid 应用uuid
     * @return AppEntity
     */
    AppEntity get(String uuid);

    /**
     * 应用详情
     * @param uuid 应用uuid
     * @return AppDetailEntity
     */
    AppDetailEntity detail(String uuid);

    /**
     * 获取应用元数据
     * @param uuid 应用uuid
     * @return AppMetaEntity
     */
    AppMetaEntity getMeta(String uuid);
}
