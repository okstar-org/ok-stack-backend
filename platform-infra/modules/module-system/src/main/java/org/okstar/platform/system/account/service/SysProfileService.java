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

package org.okstar.platform.system.account.service;


import org.okstar.platform.common.datasource.OkJpaService;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.domain.SysProfile;

import java.util.List;
import java.util.Optional;

public interface SysProfileService extends OkJpaService<SysProfile> {

    void update(SysAccount sysAccount, SysProfile sysProfile);

    SysProfile loadByUsername(String username);

    SysProfile loadByAccount(Long accountId);

    List<SysAccount> getByFirstName(String firstName);

    List<SysAccount> getByLastName(String lastName);

    List<SysAccount> getByPersonalName(String personalName);

    void syncDb2Ldap(String username);

    SysProfile loadProfile(Long accountId);

    Optional<SysProfile> getProfile(Long accountId);
}
