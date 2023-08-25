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

package org.okstar.platform.common.security.aspect;

import io.vertx.core.http.HttpServerRequest;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.okstar.platform.common.core.exception.PreAuthorizeException;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.security.annotation.PreAuthorize;
import org.okstar.platform.common.security.domain.LoginUser;
import org.okstar.platform.common.security.service.TokenService;

import javax.inject.Inject;
import javax.ws.rs.core.Context;
import java.lang.reflect.Method;
import java.util.Collection;


/**
 * 自定义权限实现
 *
 *
 */
@Aspect
public class PreAuthorizeAspect
{
    @Inject
    TokenService tokenService;
    @Context
    HttpServerRequest request;

    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    /** 管理员角色权限标识 */
    private static final String SUPER_ADMIN = "admin";

    /** 数组为0时 */
    private static final Integer ARRAY_EMPTY = 0;

    @Around("@annotation(annotation.org.okstar.platform.common.security.PreAuthorize)")
    public Object around(ProceedingJoinPoint point) throws Throwable
    {
        Signature signature = point.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        PreAuthorize annotation = method.getAnnotation(PreAuthorize.class);
        if (annotation == null)
        {
            return point.proceed();
        }

        if (OkStringUtil.isNotEmpty(annotation.hasPermi()))
        {
            if (hasPermi(annotation.hasPermi()))
            {
                return point.proceed();
            }
            throw new PreAuthorizeException();
        }
        else if (OkStringUtil.isNotEmpty(annotation.lacksPermi()))
        {
            if (lacksPermi(annotation.lacksPermi()))
            {
                return point.proceed();
            }
            throw new PreAuthorizeException();
        }
        else if (ARRAY_EMPTY < annotation.hasAnyPermi().length)
        {
            if (hasAnyPermi(annotation.hasAnyPermi()))
            {
                return point.proceed();
            }
            throw new PreAuthorizeException();
        }
        else if (OkStringUtil.isNotEmpty(annotation.hasRole()))
        {
            if (hasRole(annotation.hasRole()))
            {
                return point.proceed();
            }
            throw new PreAuthorizeException();
        }
        else if (OkStringUtil.isNotEmpty(annotation.lacksRole()))
        {
            if (lacksRole(annotation.lacksRole()))
            {
                return point.proceed();
            }
            throw new PreAuthorizeException();
        }
        else if (ARRAY_EMPTY < annotation.hasAnyRoles().length)
        {
            if (hasAnyRoles(annotation.hasAnyRoles()))
            {
                return point.proceed();
            }
            throw new PreAuthorizeException();
        }

        return point.proceed();
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission)
    {
        LoginUser userInfo = tokenService.getLoginUser(request);
        if (OkStringUtil.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getPermissions()))
        {
            return false;
        }
        return hasPermissions(userInfo.getPermissions(), permission);
    }

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     *
     * @param permission 权限字符串
     * @return 用户是否不具备某权限
     */
    public boolean lacksPermi(String permission)
    {
        return !hasPermi(permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String[] permissions)
    {
        LoginUser userInfo = tokenService.getLoginUser(request);
        if (OkStringUtil.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getPermissions()))
        {
            return false;
        }
        Collection<String> authorities = userInfo.getPermissions();
        for (String permission : permissions)
        {
            if (permission != null && hasPermissions(authorities, permission))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色字符串
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role)
    {
        LoginUser userInfo = tokenService.getLoginUser(request);
        if (OkStringUtil.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getRoles()))
        {
            return false;
        }
        for (String roleKey : userInfo.getRoles())
        {
            if (SUPER_ADMIN.equals(roleKey) || roleKey.equals(role))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反。
     *
     * @param role 角色名称
     * @return 用户是否不具备某角色
     */
    public boolean lacksRole(String role)
    {
        return !hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String[] roles)
    {
        LoginUser userInfo = tokenService.getLoginUser(request);
        if (OkStringUtil.isNull(userInfo) || CollectionUtils.isEmpty(userInfo.getRoles()))
        {
            return false;
        }
        for (String role : roles)
        {
            if (hasRole(role))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Collection<String> authorities, String permission)
    {
        return authorities.stream().filter(OkStringUtil::hasText)
                .anyMatch(x -> ALL_PERMISSION.contains(x));
    }
}
