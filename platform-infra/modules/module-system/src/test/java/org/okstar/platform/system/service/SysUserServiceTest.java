/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
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

package org.okstar.platform.system.service;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.okstar.platform.system.domain.SysUser;

import javax.inject.Inject;
import java.util.List;

@QuarkusTest
class SysUserServiceTest {

    @Inject
    ISysUserService sysUserService;

    @Test
    @Order(1)
    void save(){
        SysUser u = new SysUser();
        u.setUsername("test@test.com");
        u.setPassword("test@test.com");
        sysUserService.save(u);
    }

    @Test
    @Order(2)
    void findAll() {
        List<SysUser> all = sysUserService.findAll();
        Assert.assertNotNull(all);
        all.forEach(sysUser -> {
            Log.infof("user:%s", sysUser);
        });
    }
}
