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

package org.okstar.platform.system.rpc.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.system.dto.SysSetGlobalDTO;
import org.okstar.platform.system.rpc.SysSettingsRpc;
import org.okstar.platform.system.settings.domain.SysSetGlobal;
import org.okstar.platform.system.settings.service.SysBasicService;


@ApplicationScoped
public class SysSetGlobalRpcImpl implements SysSettingsRpc {

    @Inject
    SysBasicService service;

    @Override
    public SysSetGlobalDTO getGlobal() {
        SysSetGlobalDTO dto = new SysSetGlobalDTO();
        SysSetGlobal global = service.findDefaultGlobal();
        OkBeanUtils.copyPropertiesTo(global, dto);
        return dto;
    }
}
