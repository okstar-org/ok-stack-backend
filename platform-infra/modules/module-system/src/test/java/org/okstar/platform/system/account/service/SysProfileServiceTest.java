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

package org.okstar.platform.system.account.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.core.user.UserDefines;
import org.okstar.platform.system.account.domain.SysProfile;

@QuarkusTest
class SysProfileServiceTest {

    @Inject
    SysProfileService sysProfileService;

//    @Test
    void save() {
        SysProfile profile = new SysProfile();
        profile.setAccountId(51L);
        profile.setFirstName("Ok");
        profile.setLastName("Star");
        profile.setPhone("18910221510");
        profile.setEmail("okstar@gmail.com");
        profile.setAddress("ChaoYang");
        profile.setCity("Beijing");
        profile.setCountry("China");
        profile.setLanguage("zh-CN");
        profile.setIdentify(OkIdUtils.makeUuid());
        profile.setBirthday(OkDateUtils.now());
        profile.setGender(UserDefines.Gender.male);
        sysProfileService.save(profile);
    }

//    @Test
    void update() {
        SysProfile profile = sysProfileService.get(1L);
        profile.setFirstName("Ok");
        profile.setLastName("Star");
        profile.setPhone("18910221510");
        profile.setEmail("okstar@gmail.com");
        profile.setIdentify("430000000000");
        profile.setAddress("ChaoYang");
        profile.setCity("Beijing");
        profile.setCountry("China");
        profile.setLanguage("zh-CN");
        profile.setBirthday(OkDateUtils.now());
        profile.setGender(UserDefines.Gender.male);
        sysProfileService.save(profile);
    }


}