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
import org.junit.jupiter.api.Test;
import org.okstar.platform.system.domain.SysUser;
import org.okstar.platform.system.domain.SysUserPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@QuarkusTest
class O02_SysUserPasswordServiceTest {

    @Inject
    SysUserService sysUserService;
    @Inject
    SysUserPasswordService sysUserPasswordService;


    @Test
    void setNewPassword() {
        String newPassword = "abcd";
        List<SysUser> all = sysUserService.findAll();
        Assert.assertTrue(!all.isEmpty());

        all.forEach(user -> {
            //修改密码
            sysUserPasswordService.setNewPassword(user.id, newPassword);
            Log.infof("修改[%s]密码->%s", user.getUsername(), newPassword);

            //检查密码是否设置成功
            Optional<SysUserPassword> validPassword = sysUserPasswordService.lastValidPassword(user.id);
            boolean matches = new BCryptPasswordEncoder().matches(newPassword, validPassword.get().getPassword());
            Assert.assertTrue(matches);
        });

//        sysUserService.findAll().forEach(user->{
//            Log.infof("[%s]新密码=>%s",user.getUsername(), user.getPassword());
//
//        });
    }
}
