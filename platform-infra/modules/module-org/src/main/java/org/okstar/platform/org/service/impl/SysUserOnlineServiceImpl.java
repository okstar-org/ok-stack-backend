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

package org.okstar.platform.org.service.impl;

import org.okstar.platform.org.domain.SysUserOnline;
import org.okstar.platform.org.dto.SignInAttached;
import org.okstar.platform.org.mapper.SysRoleMapper;
import org.okstar.platform.org.service.ISysUserOnlineService;
import org.okstar.platform.org.vo.LoginJwtUser;
import org.springframework.util.Assert;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


/**
 * 在线用户 服务层处理
 * 
 * 
 */
@ApplicationScoped
public class SysUserOnlineServiceImpl implements ISysUserOnlineService
{
    @Inject
    private SysRoleMapper roleMapper;

    /**
     * 通过登录地址查询信息
     * 
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByIpaddr(String ipaddr, LoginJwtUser user)
    {
        return null;
    }

    /**
     * 通过用户名称查询信息
     * 
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByUserName(String userName, LoginJwtUser user)
    {

        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     * 
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    @Override
    public SysUserOnline selectOnlineByInfo(String ipaddr, String userName, LoginJwtUser user)
    {

        return null;
    }

    /**
     * 设置在线用户信息
     * 
     * @param jwtUser 用户信息
     * @return 在线用户
     */
    public SysUserOnline makeSaveOnline(LoginJwtUser jwtUser, SignInAttached signInAttached)
    {
        Assert.notNull(jwtUser, "参数异常！");

        SysUserOnline sysUserOnline = new SysUserOnline();
        sysUserOnline.setSessionId(jwtUser.getToken());
        sysUserOnline.setAccountId(jwtUser.getUserId());
        sysUserOnline.setSignInAttached(signInAttached);



        return sysUserOnline;
    }
}
