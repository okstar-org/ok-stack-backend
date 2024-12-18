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

package org.okstar.platform.system.account.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.core.account.AccountDefines;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.service.SysAccountService;
import org.okstar.platform.system.dto.SysLanguage;
import org.okstar.platform.system.resource.BaseResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * 个人语言接口
 */
@Authenticated
@Path("/profile/language")
public class SysLanguageResource extends BaseResource {

    @Inject
    SysAccountService accountService;

    @GET
    @Path("list")
    public Res<List<SysLanguage>> list() {
        List<SysLanguage> list = new ArrayList<>();
        list.add(SysLanguage.builder().label("中文（简体）").value(Locale.SIMPLIFIED_CHINESE.toLanguageTag()).build());//
        list.add(SysLanguage.builder().label("中文（繁体）").value(Locale.TRADITIONAL_CHINESE.toLanguageTag()).build());//
        list.add(SysLanguage.builder().label("English").value(Locale.US.toLanguageTag()).build());//
        return Res.ok(list);
    }

    @GET
    @Path("")
    public Res<String> getLanguage() {
        Optional<SysAccount> sysAccount = accountService.findByUsername(getUsername());
        return Res.ok(sysAccount.map(SysAccount::getLanguage).orElse(AccountDefines.DefaultLanguage));
    }

    @PUT
    @Path("")
    public Res<Boolean> putLanguage(String language) {
        accountService.saveLanguage(self(), language);
        return Res.ok(true);
    }
}
