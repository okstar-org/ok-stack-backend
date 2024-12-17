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

package org.okstar.platform.auth.rpc.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.auth.rpc.PassportRpc;
import org.okstar.platform.auth.service.PassportService;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;
import org.okstar.platform.system.dto.SysAccountDTO;

/**
 * 接入RPC服务
 */
@ApplicationScoped
public class PassportRpcImpl implements PassportRpc {

    @Inject
    PassportService passportService;

    /**
     * 注册
     * @param form
     * @return
     */
    @Override
    public RpcResult<SignUpResult> signUp(SignUpForm form) {
        try {
            SignUpResult result = passportService.signUp(form);
            return RpcResult.<SignUpResult>builder().success(true).data(result).build();
        } catch (Exception e) {
            return RpcResult.<SignUpResult>builder().success(false).msg(e.getMessage()).build();
        }
    }

    /**
     * 注销
     * @param accountId
     * @return
     */
    @Override
    public RpcResult<Boolean> signDown(Long accountId) {
        try {
            passportService.signDown(accountId);
            return RpcResult.<Boolean>builder().success(true).build();
        } catch (Exception e) {
            return RpcResult.<Boolean>builder().success(false).msg(e.getMessage()).build();
        }
    }

    @Override
    public RpcResult<SysAccountDTO> getAccount(String account) {
        try {
            SysAccountDTO account0 = passportService.getAccount(account);
            return RpcResult.<SysAccountDTO>builder().success(true).data(account0).build();
        } catch (Exception e) {
            return RpcResult.<SysAccountDTO>builder().success(false).msg(e.getMessage()).build();
        }
    }
}
