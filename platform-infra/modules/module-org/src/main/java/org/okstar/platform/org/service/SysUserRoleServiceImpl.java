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

package org.okstar.platform.org.service;

import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.mapper.SysUserRoleRepository;
import org.okstar.platform.org.rbac.domain.OrgRbacUserRole;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Inject
    private SysUserRoleRepository userRoleRepository;

    @Override
    public void save(OrgRbacUserRole orgRbacUserRole) {
        userRoleRepository.persist(orgRbacUserRole);
    }

    @Override
    public List<OrgRbacUserRole> findAll() {
        return userRoleRepository.findAll().stream().toList();
    }

    @Override
    public OkPageResult<OrgRbacUserRole> findPage(OkPageable page) {
        return null;
    }

    @Override
    public OrgRbacUserRole get(Long id) {
        return userRoleRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRoleRepository.deleteById(id);
    }

    @Override
    public void delete(OrgRbacUserRole orgRbacUserRole) {
        userRoleRepository.delete(orgRbacUserRole);
    }
    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public long countByRoleId(Long roleId) {
        return userRoleRepository.count("roleId", roleId);
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    @Override
    public long deleteByUserIds(Long roleId, Long[] userIds) {
        long c = 0;
        if (userIds == null)
            return c;
        for (int i = 0; i < userIds.length; i++) {
            c += userRoleRepository.delete("roleId = ?1 and userId = ?2",
                    roleId, userIds[i]);
        }

        return c;
    }


}
