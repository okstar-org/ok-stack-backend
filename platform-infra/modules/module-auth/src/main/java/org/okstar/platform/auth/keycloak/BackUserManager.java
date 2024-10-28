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

package org.okstar.platform.auth.keycloak;

import org.okstar.platform.system.dto.BackUser;

import java.util.List;
import java.util.Optional;


/**
 * 后端认证用户管理接口
 */
public interface BackUserManager {
    boolean hasPassword(String username);

    void resetPassword(String username, String password);

    List<BackUser> users();

    void assignRole(String username, String roleId);

    void unassignRole(String username, String roleId);

    List<BackRoleDTO> listRoles(String username);

    Optional<BackUser> getUser(String username);

    /**
     * 增加用户
     *
     * @param user
     * @return
     */
    void addUser(BackUser user);

    /**
     * 删除用户
     *
     * @param username
     * @return
     */
    boolean deleteUser(String username);

    void forgot(String username);
}
