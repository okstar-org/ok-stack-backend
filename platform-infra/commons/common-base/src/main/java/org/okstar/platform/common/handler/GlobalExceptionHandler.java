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

package org.okstar.platform.common.handler;

import org.okstar.platform.common.core.web.bean.Req;
import org.okstar.platform.common.core.web.bean.Res;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * 全局异常处理器
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
        throwable.printStackTrace();
        Res<Object> error = Res.error(Req.empty(), throwable.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error).build();
    }

}
