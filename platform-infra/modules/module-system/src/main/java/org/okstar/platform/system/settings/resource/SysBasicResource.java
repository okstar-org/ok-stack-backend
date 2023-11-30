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

package org.okstar.platform.system.settings.resource;

import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.system.settings.domain.SysBasic;
import org.okstar.platform.system.dto.SysLocale;
import org.okstar.platform.system.settings.service.SysBasicService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Path("settings/basic")
public class SysBasicResource {

    @Inject
    SysBasicService service;

    @GET
    @Path("findLocales")
    public Res<List<SysLocale>> findLocales() {
        /**
         * 中文（中国）：zh_CN
         * 中文（台湾）：zh_TW
         * 英语：en
         */
        List<SysLocale> list = new ArrayList<>();
        list.add(SysLocale.builder().label("中文（中国）").value(Locale.SIMPLIFIED_CHINESE.toString()).build());//
        list.add(SysLocale.builder().label("中文（台湾）").value(Locale.TRADITIONAL_CHINESE.toString()).build());//
        list.add(SysLocale.builder().label("English").value(Locale.ENGLISH.toString()).build());//
        return Res.ok(list);
    }

    @GET
    @Path("find")
    public Res<SysBasic> find() {
        return Res.ok(service.findDefault());
    }

    @PUT
    @Path("update")
    public void update(SysBasic basic) {
        service.save(basic);
    }
}
