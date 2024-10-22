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
import org.okstar.platform.system.dto.SysConfImDTO;
import org.okstar.platform.system.dto.SysConfStackDTO;
import org.okstar.platform.system.rpc.SysConfIntegrationRpc;
import org.okstar.platform.system.conf.domain.SysConfIntegration;
import org.okstar.platform.system.conf.service.SysConfIntegrationService;


@ApplicationScoped
public class SysConfIntegrationRpcImpl implements SysConfIntegrationRpc {

    @Inject
    SysConfIntegrationService service;

    @Override
    public org.okstar.platform.system.dto.SysConfIntegrationDTO getIntegrationConf() {
        SysConfIntegration integration = service.find();

        org.okstar.platform.system.dto.SysConfIntegrationDTO dto = new org.okstar.platform.system.dto.SysConfIntegrationDTO();

        SysConfImDTO im = new SysConfImDTO();
        im.setHost(integration.getIm().getHost());
        im.setPort(integration.getIm().getPort());
        im.setAdminPort(integration.getIm().getAdminPort());
        im.setApiSecretKey(integration.getIm().getApiSecret());
        dto.setIm(im);

        SysConfStackDTO stack = new SysConfStackDTO();
        stack.setFqdn(integration.getStack().getFqdn());
        dto.setStack(stack);

        return dto;
    }

    @Override
    public void uploadConf() {
        SysConfIntegration integration = service.find();
        service.uploadConf(integration);
    }
}
