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

package org.okstar.platform.system.rpc;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.system.conf.service.SysPropertyService;
import org.okstar.platform.system.dto.SysPropertyDTO;

import java.util.List;

/**
 * Properties RPC实现
 */
@Transactional
@ApplicationScoped
public class SysPropertyRpcImpl implements SysPropertyRpc {
    @Inject
    SysPropertyService sysPropertyService;

    @Override
    public void save(SysPropertyDTO dto) {
        sysPropertyService.save(dto);
    }

    @Override
    public void deleteByGroup(String group) {
        sysPropertyService.deleteByGroup(group);
    }

    @Override
    public List<SysPropertyDTO> getByGroup(String group) {
        return sysPropertyService.findByGroup(group).stream()
                .map(sysPropertyService::toDTO).toList();
    }


    @Override
    public List<SysPropertyDTO> getByKey(String group, String domain, String key) {
        return sysPropertyService.findByKey(group, domain, key).stream()
                .map(sysPropertyService::toDTO).toList();
    }
}
