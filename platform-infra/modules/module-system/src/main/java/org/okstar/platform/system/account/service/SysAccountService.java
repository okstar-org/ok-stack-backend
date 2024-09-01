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


import org.okstar.platform.common.datasource.OkJpaService;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysAccountBind;
import org.okstar.platform.system.account.domain.SysAccountPassword;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;

import java.util.List;
import java.util.Optional;

/**
 * 用户业务层
 */
public interface SysAccountService extends OkJpaService<SysAccount> {


    /**
     * //通过帐号（邮箱、手机号）搜索
     *
     * @param account
     * @return
     */
    Optional<SysAccount> findByAccount(String account);

    Optional<SysAccount> findByUsername(String username);

    Optional<SysAccount> findByBind(AccountDefines.BindType bindType, String iso, String bindValue);

    List<SysAccount> findByNickname(String nickname);

    SysAccountDTO toAccount0(SysAccount account);

    SignUpResult signUp(SignUpForm signUpForm);

    void signDown(Long accountId);

    void save(SysAccount sysUser);


    Optional<SysAccountPassword> lastPassword(Long accountId);

    SysAccount loadByUsername(String username);

    List<SysAccountBind> listBind(Long id);

    void setCert(Long id, String cert);
}
