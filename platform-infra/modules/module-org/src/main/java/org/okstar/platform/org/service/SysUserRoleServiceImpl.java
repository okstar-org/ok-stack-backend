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
import org.okstar.platform.org.domain.SysUserRole;
import org.okstar.platform.org.mapper.SysUserRoleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SysUserRoleServiceImpl implements SysUserRoleService {

    @Inject
    private SysUserRoleRepository userRoleRepository;

    @Override
    public void save(SysUserRole sysUserRole) {
        userRoleRepository.persist(sysUserRole);
    }

    @Override
    public List<SysUserRole> findAll() {
        return userRoleRepository.findAll().stream().toList();
    }

    @Override
    public OkPageResult<SysUserRole> findPage(OkPageable page) {
        return null;
    }

    @Override
    public SysUserRole get(Long id) {
        return userRoleRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        userRoleRepository.deleteById(id);
    }

    @Override
    public void delete(SysUserRole sysUserRole) {
        userRoleRepository.delete(sysUserRole);
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

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要删除的用户数据ID
     */
    @Override
    public void insertUsers(Long roleId, Long[] userIds) {
        if (userIds == null) {
            return;
        }

        // 新增用户与角色管理
        List<SysUserRole> list = Arrays.stream(userIds)
                .map(userId -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    return ur;
                }).collect(Collectors.toList());

        userRoleRepository.persist(list);
    }
}
