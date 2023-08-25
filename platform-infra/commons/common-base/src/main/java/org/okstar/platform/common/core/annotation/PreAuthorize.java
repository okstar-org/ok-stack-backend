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

package org.okstar.platform.common.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限注解
 * 
 * 
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorize
{
    /**
     * 验证用户是否具备某权限
     */
    String hasPermi() default "";

    /**
     * 验证用户是否不具备某权限，与 hasPermi逻辑相反
     */
    String lacksPermi() default "";

    /**
     * 验证用户是否具有以下任意一个权限
     */
    String[] hasAnyPermi() default {};

    /**
     * 判断用户是否拥有某个角色
     */
    String hasRole() default "";

    /**
     * 验证用户是否不具备某角色，与 isRole逻辑相反
     */
    String lacksRole() default "";

    /**
     * 验证用户是否具有以下任意一个角色
     */
    String[] hasAnyRoles() default {};
}
