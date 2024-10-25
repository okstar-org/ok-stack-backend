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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.dto.SysAccountDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class SysUserSearchServiceImpl implements SysUserSearchService {
    @Inject
    private SysAccountService accountService;
    @Inject
    private SysProfileService profileService;

    @Override
    public List<SysAccountDTO> search(String query) {
        List<SysAccountDTO> list = new ArrayList<>();

        //帐号搜索
        Optional<SysAccount> account = accountService.findByAccount(query);
        account.ifPresent(e -> {
            list.add(accountService.toAccount0(e));
        });

        //用户名搜索
        accountService.findByUsername(query).ifPresent(e -> {
            list.add(accountService.toAccount0(e));
        });

        //昵称搜索
        accountService.findByNickname(query)
                .stream().map(e -> accountService.toAccount0(e))
                .forEach(list::add);

        //姓名搜索
        profileService.getByFirstName(query).stream().map(e -> accountService.toAccount0(e)).forEach(list::add);
        profileService.getByLastName(query).stream().map(e -> accountService.toAccount0(e)).forEach(list::add);
        profileService.getByPersonalName(query).stream().map(e -> accountService.toAccount0(e)).forEach(list::add);

        return list.stream().distinct().toList();
    }
}
