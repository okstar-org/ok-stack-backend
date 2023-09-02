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

package org.okstar.platform.org.mapper;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.okstar.platform.org.domain.SysUserRole;

import javax.enterprise.context.ApplicationScoped;

/**
 * 用户与角色关联表
 * 
 * 
 */
@ApplicationScoped
public class SysUserRoleRepository implements PanacheRepository<SysUserRole>
{

}
