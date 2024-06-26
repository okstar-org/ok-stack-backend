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
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.system.sign.SignUpForm;
import org.okstar.platform.system.sign.SignUpResult;
import org.okstar.platform.system.vo.SysAccount0;




@ApplicationScoped
public class PassportRpcImpl implements PassportRpc {

    @Inject
    PassportService passportService;


    @Override
    public RpcResult<SignUpResult> signUp(SignUpForm form) {
        try {
            SignUpResult result = passportService.signUp(form);
            return RpcResult.<SignUpResult>builder().success(true).data(result).build();
        } catch (Exception e) {
            return RpcResult.<SignUpResult>builder().success(false).msg(e.getMessage()).build();
        }
    }

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
    public RpcResult<SysAccount0> getAccount(String account) {
        try {
            SysAccount0 account0 = passportService.getAccount(account);
            return RpcResult.<SysAccount0>builder().success(true).data(account0).build();
        } catch (Exception e) {
            return RpcResult.<SysAccount0>builder().success(false).msg(e.getMessage()).build();
        }
    }
}
