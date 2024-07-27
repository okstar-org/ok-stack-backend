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
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.system.dto.SysLanguage;
import org.okstar.platform.system.resource.BaseResource;
import org.okstar.platform.system.settings.domain.SysConfPersonal;
import org.okstar.platform.system.settings.service.SysConfPersonalService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 个人设置
 */
@Path("conf/personal")
public class SysConfPersonalResource extends BaseResource {

    @Inject
    SysConfPersonalService service;

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
    @Path("")
    public Res<SysConfPersonal> get() {
        SysConfPersonal personal = service.findDefault(self());
        return Res.ok(personal);
    }

    @PUT
    @Path("")
    public void put(SysConfPersonal personal) {
        service.save(self(), personal);
    }
}
