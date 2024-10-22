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

import org.okstar.platform.system.conf.domain.SysConfIntegrationKeycloak;
import org.okstar.platform.system.dto.SysKeycloakConfDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//@QuarkusTest
class SysOidcServiceTest {

//    @Inject
    SysKeycloakService sysKeycloakService;

//    @Test
    void initKeycloakConfig() {
        var config = sysKeycloakService.initConfig();
        System.out.println(config);
        assertNotNull(config);
    }

//    @Test
    void testKeycloakConfig() {
        String tested = sysKeycloakService.testConfig();
        System.out.println(tested);
        assertNotNull(tested);
    }

//    @Test
    void clearConfig() {
        sysKeycloakService.clearConfig();
    }

//    @Test
    void initRealm() {
        SysConfIntegrationKeycloak config = sysKeycloakService.getConfig();
        String test = sysKeycloakService.initRealm(config, "okstar");
        assertNotNull(test);
    }

//    @Test
    void listRealms(){
        List<String> realms = sysKeycloakService.listRealms();
        System.out.println(realms);
        assertNotNull(realms);
    }

//    @Test
    void removeRealm() {
        sysKeycloakService.removeRealm();
    }

//    @Test
    void getOidcConf(){
        SysKeycloakConfDTO oidcConfig = sysKeycloakService.getStackConfig();
        System.out.println(oidcConfig);
        assertNotNull(oidcConfig);
    }

}