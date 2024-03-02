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

package org.okstar.platform.system.settings.resource;

import jakarta.inject.Inject;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.dto.SysLanguage;
import org.okstar.platform.system.resource.BaseResource;
import org.okstar.platform.system.settings.domain.SysSetGlobal;
import org.okstar.platform.system.settings.domain.SysSetPersonal;
import org.okstar.platform.system.settings.service.SysBasicService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Path("settings/basic")
public class SysBasicResource extends BaseResource {

    @Inject
    SysBasicService service;


    @GET
    @Path("languages")
    public Res<List<SysLanguage>> languages() {
        List<SysLanguage> list = new ArrayList<>();
        list.add(SysLanguage.builder().label("中文（简体）").value(Locale.SIMPLIFIED_CHINESE.toLanguageTag()).build());//
        list.add(SysLanguage.builder().label("中文（繁体）").value(Locale.TRADITIONAL_CHINESE.toLanguageTag()).build());//
        list.add(SysLanguage.builder().label("English").value(Locale.US.toLanguageTag()).build());//
        return Res.ok(list);
    }

    @GET
    @Path("global")
    public Res<SysSetGlobal> getGlobal() {
        return Res.ok(service.findDefaultGlobal());
    }

    @PUT
    @Path("global")
    public void updateGlobal(SysSetGlobal global) {
        /**
         * 保存设置
         */
        service.save(global);
    }

    @GET
    @Path("personal")
    public Res<SysSetPersonal> getPersonal() {
        SysAccount account = self();
        return Res.ok(service.findDefaultPersonal(account));
    }

    @PUT
    @Path("personal")
    public void updatePersonal(SysSetPersonal basic) {
        service.savePersonal(basic);
    }
}
