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

package org.okstar.platform.system.settings.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.system.settings.domain.*;
import org.okstar.platform.system.settings.mapper.SysSetKvMapper;

import java.util.List;

@Transactional
@ApplicationScoped
public class SysConfIntegrationServiceImpl implements SysConfIntegrationService {

    @Inject
    SysSetKvMapper kvMapper;

    @Override
    public SysConfIntegration find() {
        SysConfIntegration integration = new SysConfIntegration();

        //IM
        SysConfIntegrationIm im = new SysConfIntegrationIm();
        im.addProperties(kvMapper.findByGroup(im.getGroup()));
        integration.setIm(im);

        //Stack
        SysConfIntegrationStack stack = new SysConfIntegrationStack();
        stack.addProperties(kvMapper.findByGroup(stack.getGroup()));
        integration.setStack(stack);

        //Keycloak
        SysConfIntegrationKeycloak keycloak = new SysConfIntegrationKeycloak();
        keycloak.addProperties(kvMapper.findByGroup(keycloak.getGroup()));
        integration.setKeycloak(keycloak);
        return integration;
    }

    @Override
    public void save(SysConfIntegration integration) {
        SysConfIntegrationIm im = integration.getIm();
        List<SysProperty> imProperties = List.of(
                SysProperty.builder()
                        .grouping(im.getGroup())
                        .k("host")
                        .v(im.getHost())
                        .build(),

                SysProperty.builder()
                        .grouping(im.getGroup())
                        .k("api-secret")
                        .v(im.getApiSecret())
                        .build(),

                SysProperty.builder()
                        .grouping(im.getGroup())
                        .k("admin-port")
                        .v(String.valueOf(im.getAdminPort()))
                        .build()
        );
        kvMapper.persist(imProperties);

        //stack
        SysConfIntegrationStack stack = integration.getStack();
        List<SysProperty> stackProperties = List.of(
                SysProperty.builder()
                        .grouping(stack.getGroup())
                        .k("fqdn")
                        .v(stack.getFqdn())
                        .build());
        kvMapper.persist(stackProperties);
    }
}
