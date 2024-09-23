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
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.system.dto.SysPropertyDTO;
import org.okstar.platform.system.settings.domain.SysProperty;
import org.okstar.platform.system.settings.service.SysPropertyService;

import java.util.List;

@Transactional
@ApplicationScoped
public class SysPropertyRpcImpl implements SysPropertyRpc {
    @Inject
    SysPropertyService sysPropertyService;

    @Override
    public void save(SysPropertyDTO dto) {

        List<SysProperty> list = dto.getDomain() == null ?
                sysPropertyService.findByKey(dto.getGrouping(), dto.getK()) :
                sysPropertyService.findByKey(dto.getGrouping(), dto.getDomain(), dto.getK());

        if (list.isEmpty()) {
            //create
            SysProperty t = new SysProperty();
            OkBeanUtils.copyPropertiesTo(dto, t);
            sysPropertyService.create(t, 1L);
        } else {
            //update
            list.forEach(exist -> {
                exist.setV(dto.getV());
                sysPropertyService.update(exist, 1L);
            });
        }

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
