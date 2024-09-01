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

package org.okstar.platform.billing.resource;

import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.core.web.resource.OkCommonResource;
import org.okstar.platform.common.rpc.RpcAssert;
import org.okstar.platform.system.dto.SysAccountDTO;
import org.okstar.platform.system.rpc.SysAccountRpc;

public class BillingBaseResource extends OkCommonResource {

    @Inject @RestClient
    SysAccountRpc sysAccountRpc;

    protected Long loadUserId() {
        SysAccountDTO account0 = RpcAssert.isTrue(sysAccountRpc.findByUsername(getUsername()));
        return account0.getId();
    }
}
