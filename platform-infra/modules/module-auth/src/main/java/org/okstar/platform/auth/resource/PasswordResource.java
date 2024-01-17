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

package org.okstar.platform.auth.resource;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.okstar.platform.auth.service.PassportService;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.common.resource.OkCommonResource;
import org.okstar.platform.system.sign.PasswordUpdateForm;

/**
 * 密码
 */
@Path("password")
public class PasswordResource extends OkCommonResource {


    @Inject
    PassportService passportService;

    /**
     * 修改密码
     *
     * @param updateForm
     * @return
     */
    @PUT
    @Path("update")
    public Res<Boolean> update(PasswordUpdateForm updateForm) {
        Log.infof("update:%s", updateForm);
        updateForm.setUsername(getUsername());
        passportService.updatePassword(updateForm);
        return Res.ok(true);
    }


}
