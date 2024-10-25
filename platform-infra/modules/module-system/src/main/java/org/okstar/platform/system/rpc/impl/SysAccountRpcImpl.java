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
import jakarta.transaction.Transactional;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.phone.OkPhoneUtils;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysAccountBind;
import org.okstar.platform.system.account.service.SysAccountService;
import org.okstar.platform.system.account.service.SysProfileService;
import org.okstar.platform.system.dto.SysAccountBindDTO;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;

import java.util.List;
import java.util.Optional;

@Transactional
@ApplicationScoped
public class SysAccountRpcImpl implements SysAccountRpc {

    @Inject
    SysAccountService accountService;
    @Inject
    SysProfileService profileService;

    @Override
    public RpcResult<String> lastPassword(Long accountId) {
        var resultDto = accountService.lastPassword(accountId);
        if (resultDto.isEmpty()) {
            return RpcResult.<String>builder().success(false).build();
        }
        return RpcResult.<String>builder().success(true).data(resultDto.get().getPassword()).build();
    }


    @Override
    public RpcResult<SignUpResult> signUp(SignUpForm signUpForm) {
        try {
            SignUpResult resultDto = accountService.signUp(signUpForm);
            return RpcResult.<SignUpResult>builder().success(true).data(resultDto).build();
        } catch (Exception e) {
            return RpcResult.<SignUpResult>builder().success(false).msg(e.getMessage()).build();
        }
    }

    @Override
    public void signDown(Long accountId) {
        accountService.signDown(accountId);
    }


    @Override
    public Optional<SysAccountDTO> getByAccount(String account) {
        return accountService.findByAccount(account).map(e -> accountService.toAccount0(e));
    }

    @Override
    public Optional<SysAccountDTO> findByBind(AccountDefines.BindType type, String bindValue) {
        return accountService.findByBind(type, bindValue).map(e -> accountService.toAccount0(e));
    }

    @Override
    public Optional<SysAccountDTO> findByEmail(String email) {
        return findByBind(AccountDefines.BindType.email, email);
    }

    public Optional<SysAccountDTO> findByPhone(String phone, String iso) {
        return findByBind(AccountDefines.BindType.phone, OkPhoneUtils.canonical(phone, iso));
    }

    @Override
    public Optional<SysAccountDTO> findByUsername(String username) {
        return accountService.findByUsername(username).map(e -> accountService.toAccount0(e));
    }

    @Override
    public SysAccountDTO findById(Long id) {
        SysAccount account = accountService.get(id);
        return accountService.toAccount0(account);
    }

    @Override
    public void setCert(Long id, String cert) {
        accountService.setCert(id, cert);
    }

    @Override
    public RpcResult<List<SysAccountBindDTO>> getBinds(Long id) {
        List<SysAccountBind> binds = accountService.listBind(id);
        List<SysAccountBindDTO> list = binds.stream().map(e -> {
            SysAccountBindDTO dto = new SysAccountBindDTO();
            OkBeanUtils.copyPropertiesTo(e, dto);
            return dto;
        }).toList();
        return RpcResult.success(list);
    }

    @Override
    public void setUid(String username, String uid) {
        accountService.setUid(username, uid);
    }

    @Override
    public void syncDb2Ldap(String username) {
        accountService.syncDb2Ldap(username);
        profileService.syncDb2Ldap(username);
    }


}
