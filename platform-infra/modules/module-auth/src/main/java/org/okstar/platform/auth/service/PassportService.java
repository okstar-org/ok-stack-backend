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

package org.okstar.platform.auth.service;


import org.okstar.platform.system.sign.*;
import org.okstar.platform.system.dto.SysAccountDTO;

/**
 * 接入服务接口
 */
public interface PassportService {
    /**
     * 注册
     * @param signUpForm
     * @return
     */
    SignUpResult signUp(SignUpForm signUpForm);

    /**
     * 注销
     * @param accountId
     */
    void signDown(Long accountId);

    /**
     * 登录
     * @param signInForm
     * @return
     */
    AuthorizationResult signIn(SignInForm signInForm);

    /**
     * 刷新
     * @param refreshToken
     * @return
     */
    AuthorizationResult refresh(String refreshToken);

    /**
     * 退出
     * @param accessToken
     */
    void signOut(String accessToken);

    /**
     * 获取帐号
     * @param account
     * @return
     */
    SysAccountDTO getAccount(String account);

    /**
     * 更新密码
     * @param updateForm
     */
    void updatePassword(PasswordUpdateForm updateForm);

    /**
     * 忘记密码
     * @param form
     */
    void forgot(ForgotForm form);
}
