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

package org.okstar.platform.system.account.service;


import org.okstar.platform.org.vo.LoginJwtUser;
import org.okstar.platform.system.account.domain.SysAccountOnline;
import org.okstar.platform.system.account.vo.SignInAttached;

/**
 * 在线用户 服务层
 * 
 *
 */
public interface ISysAccountOnlineService
{
    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    SysAccountOnline selectOnlineByIpaddr(String ipaddr, LoginJwtUser user);

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    SysAccountOnline selectOnlineByUserName(String userName, LoginJwtUser user);

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    SysAccountOnline selectOnlineByInfo(String ipaddr, String userName, LoginJwtUser user);

    /**
     * 设置在线用户信息
     * 
     * @param user 用户信息
     * @return 在线用户
     */
    SysAccountOnline makeSaveOnline(LoginJwtUser user, SignInAttached signInAttached);
}
