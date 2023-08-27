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

import io.smallrye.common.constraint.Assert;
import lombok.SneakyThrows;
import org.okstar.platform.common.core.utils.DateUtils;
import org.okstar.platform.common.datasource.OkAbsService;
import org.okstar.platform.system.domain.SysUser;
import org.okstar.platform.system.domain.SysUserPassword;
import org.okstar.platform.system.mapper.SysUserPasswordRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SysUserPasswordServiceImpl extends OkAbsService implements SysUserPasswordService {

    @Inject
    SysUserPasswordRepository repository;
    @Inject
    SysUserService sysUserService;
    @Inject
    MyRequestScopedBean requestBean;

    @Override
    public SysUserPassword save(SysUserPassword sysUserPassword) {
        repository.persist(sysUserPassword);
        return sysUserPassword;
    }

    @Override
    public List<SysUserPassword> findAll() {
        return repository.findAll().stream().toList();
    }

    @Override
    public SysUserPassword get(Long aLong) {
        return repository.findById(aLong);
    }

    @Override
    public void deleteById(Long aLong) {
        repository.deleteById(aLong);
    }

    @Override
    public void delete(SysUserPassword sysUserPassword) {
        repository.delete(sysUserPassword);
    }

    @SneakyThrows
    @Override
    public void setNewPassword(Long userId, String newPassword) {
        SysUser user = sysUserService.get(userId);
        Assert.assertNotNull(user);

        findAll().forEach(up -> {
            up.setDisabled(true);
            up.setUpdateAt(DateUtils.getNowDate());
            repository.persist(up);
        });

        var enPwd = new BCryptPasswordEncoder().encode(newPassword);
        SysUserPassword userPassword = new SysUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword(enPwd);
        userPassword.setDisabled(false);
        repository.persist(userPassword);
    }

    @Override
    public Optional<SysUserPassword> lastValidPassword(Long userId) {
        return repository.find("user.id = ?1 and disabled = false", userId).stream().findFirst();
    }
}
