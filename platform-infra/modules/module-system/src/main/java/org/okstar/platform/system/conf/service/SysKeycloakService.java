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

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.okstar.platform.system.account.domain.SysProfile;
import org.okstar.platform.system.dto.SysConfIntegrationKeycloak;
import org.okstar.platform.system.dto.SysKeycloakConfDTO;

import java.util.List;

public interface SysKeycloakService {

    String getAuthServerUrl();

    String getRealm();

    void updateUserProfile(String uid, SysProfile sysProfile);

    List<String> listRealms();

    void removeRealm();

    String initRealm(SysConfIntegrationKeycloak conf, String realm);

    Keycloak openKeycloak();

    Keycloak openKeycloak(SysConfIntegrationKeycloak config);

    ClientRepresentation getClient(String realm, String clientId);

    String testConfig();

    void clearConfig();


    SysConfIntegrationKeycloak getConfig();

    SysConfIntegrationKeycloak initConfig();

    SysKeycloakConfDTO getStackConfig();

    SysKeycloakConfDTO getAdminConfig();
}
