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

package org.okstar.platform.system.rpc.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.common.core.defined.AccountDefines;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.service.SysAccountService;
import org.okstar.platform.system.rpc.SysAccountRpc;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;
import org.okstar.platform.system.vo.SysAccount0;

import static org.okstar.platform.common.core.defined.AccountDefines.BindType.email;
import static org.okstar.platform.common.core.defined.AccountDefines.BindType.phone;


@ApplicationScoped
public class SysAccountRpcImpl implements SysAccountRpc {

    @Inject
    SysAccountService userService;

    @Override
    public RpcResult<String> lastPassword(Long accountId) {
        var resultDto = userService.lastPassword(accountId);
        if (resultDto.isEmpty()) {
            return RpcResult.<String>builder().success(false).build();
        }
        return RpcResult.<String>builder().success(true).data(resultDto.get().getPassword()).build();
    }


    @Override
    public RpcResult<SignUpResult> signUp(SignUpForm signUpForm) {
        try {
            SignUpResult resultDto = userService.signUp(signUpForm);
            return RpcResult.<SignUpResult>builder().success(true).data(resultDto).build();
        } catch (Exception e) {
            return RpcResult.<SignUpResult>builder().success(false).msg(e.getMessage()).build();
        }
    }

    @Override
    public RpcResult<Boolean> signDown(Long accountId) {
        try {
            userService.signDown(accountId);
            return RpcResult.<Boolean>builder().success(true).data(true).build();
        } catch (Exception e) {
            return RpcResult.<Boolean>builder().success(false).msg(e.getMessage()).build();
        }
    }

    @Override
    public RpcResult<SysAccount0> findByBind(String iso, AccountDefines.BindType type, String bindValue) {
        try {
            var sysUser = userService.findByBind(iso, type, bindValue);
            if (sysUser == null)
                return RpcResult.<SysAccount0>builder().success(true).build();

            SysAccount0 dto = new SysAccount0();
            OkBeanUtils.copyPropertiesTo(sysUser, dto);
            return RpcResult.<SysAccount0>builder().data(dto).success(true).build();
        } catch (Exception e) {
            return RpcResult.<SysAccount0>builder().success(false).msg(e.getMessage()).build();
        }
    }

    @Override
    public RpcResult<SysAccount0> findByUsername(String username) {
        var sysUser = userService.findByUsername(username);
        if (sysUser.isEmpty())
            return RpcResult.<SysAccount0>builder().success(true).build();

        SysAccount0 dto = new SysAccount0();
        OkBeanUtils.copyPropertiesTo(sysUser.get(), dto);
        return RpcResult.<SysAccount0>builder().data(dto).success(true).build();
    }

    @Override
    public RpcResult<SysAccount0> findById(Long id) {
        SysAccount account = userService.get(id);
        SysAccount0 dto = new SysAccount0();
        OkBeanUtils.copyPropertiesTo(account, dto);
        return RpcResult.success(dto);
    }

    @Override
    public RpcResult<SysAccount0> findByAccount(String account) {
        AccountDefines.BindType bindType = account.indexOf("@") > 0 ? email : phone;  //
        SysAccount0 account0 = RpcAssert.isTrue(findByBind(AccountDefines.DefaultISO, bindType, account));
        if (account0 == null) {
            throw new OkRuntimeException("Account is not exist");
        }
        return RpcResult.success(account0);
    }

    @Override
    public void setCert(Long id, String cert) {
        userService.setCert(id, cert);
    }
}
