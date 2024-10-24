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

package org.okstar.platform.system.resource;


import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.okstar.common.storage.dto.UploadDTO;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.system.account.domain.SysAccount;
import org.okstar.platform.system.account.service.SysAccountService;
import org.okstar.platform.system.service.SysUploadService;

@Path("upload")
public class SysUploadResource extends BaseResource {

    @Inject
    SysUploadService sysUploadService;
    @Inject
    SysAccountService accountService;

    @POST
    @Path("favicon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Res<String> uploadFavicon(@MultipartForm UploadDTO uploadDTO) {
        try {
            String x = sysUploadService.uploadFavicon(uploadDTO);
            return Res.ok(x);
        } catch (Exception e) {
            return Res.error(e.getMessage());
        }
    }


    @PUT
    @Path("logo")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Res<String> uploadLogo(@MultipartForm UploadDTO uploadDTO) {
        try {
            String x = sysUploadService.uploadLogo(uploadDTO);
            return Res.ok(x);
        } catch (Exception e) {
            return Res.error(e.getMessage());
        }
    }


    @PUT
    @Path("avatar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Res<String> uploadAvatar(@MultipartForm UploadDTO uploadDTO) {
        try {
            SysAccount self = self();
            String url = sysUploadService.uploadAvatar(self, uploadDTO);
            accountService.saveAvatar(self, url);
            return Res.ok(url);
        } catch (Exception e) {
            return Res.error(e.getMessage());
        }
    }

}
