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

package org.okstar.platform.system.conf.service;

import org.okstar.platform.common.datasource.OkJpaService;
import org.okstar.platform.system.dto.SysPropertyDTO;
import org.okstar.platform.system.conf.domain.SysProperty;

import java.util.List;
import java.util.Optional;

/**
 * Properties配置服务层
 */
public interface SysPropertyService extends OkJpaService<SysProperty> {
    void deleteByGroup(String group);

    List<SysProperty> findByGroup(String group);

    List<SysProperty> findByGroupDomain(String group, String domain);

    Optional<SysProperty> findByKey(String group, String k);

    Optional<SysProperty> findByKey(String group, String domain, String k);

    SysPropertyDTO toDTO(SysProperty p);

    List<SysPropertyDTO> toDTOs(List<SysProperty> ps);

    SysProperty save(SysPropertyDTO dto);
}
