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
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysAccountBind;
import org.okstar.platform.system.account.service.SysAccountService;
import org.okstar.platform.system.account.service.SysUserSearchService;
import org.okstar.platform.system.dto.SysAccountBindDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;
import org.okstar.platform.system.vo.SysAccount0;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class SysAccountRpcImpl implements SysAccountRpc {

    @Inject
    SysAccountService accountService;
    @Inject
    private SysUserSearchService sysUserSearchService;

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
    public RpcResult<Boolean> signDown(Long accountId) {
        try {
            accountService.signDown(accountId);
            return RpcResult.<Boolean>builder().success(true).data(true).build();
        } catch (Exception e) {
            return RpcResult.<Boolean>builder().success(false).msg(e.getMessage()).build();
        }
    }


    @Override
    public RpcResult<SysAccount0> getByAccount(String account) {
        Optional<SysAccount> sysAccount = accountService.findByAccount(account);
        if (sysAccount.isPresent()) {
            return RpcResult.success(accountService.toAccount0(sysAccount.get()));
        }
        return RpcResult.<SysAccount0>builder().success(true).build();
    }

    @Override
    public RpcResult<SysAccount0> findByBind(AccountDefines.BindType type, String iso, String bindValue) {
        var sysAccount = accountService.findByBind(type, iso, bindValue);
        return sysAccount.map(e -> {
            SysAccount0 dto = accountService.toAccount0(sysAccount.get());
            return RpcResult.<SysAccount0>builder().data(dto).success(true).build();
        }).orElse(RpcResult.<SysAccount0>builder().success(true).build());
    }

    @Override
    public RpcResult<SysAccount0> findByEmail(AccountDefines.BindType type, String email) {
        return findByBind(type, null, email);
    }

    @Override
    public RpcResult<SysAccount0> findByUsername(String username) {
        var sysAccount = accountService.findByUsername(username);
        return sysAccount.map(e -> {
            SysAccount0 dto = accountService.toAccount0(sysAccount.get());
            return RpcResult.<SysAccount0>builder().data(dto).success(true).build();
        }).orElse(RpcResult.<SysAccount0>builder().success(true).build());
    }

    @Override
    public RpcResult<SysAccount0> findById(Long id) {
        SysAccount account = accountService.get(id);
        SysAccount0 dto = accountService.toAccount0(account);
        return RpcResult.success(dto);
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
    public List<SysAccount0> search(String query) {
        return sysUserSearchService.search(query);
    }

}
