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

package org.okstar.platform.org.rpc.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.okstar.platform.core.rpc.RpcResult;
import org.okstar.platform.org.dto.OrgEmployee;
import org.okstar.platform.org.rpc.OrgStaffRpc;
import org.okstar.platform.org.staff.service.OrgStaffService;

import java.util.List;


/**
 * 组织人员RPC实现
 */
@ApplicationScoped
public class OrgStaffRpcImpl implements OrgStaffRpc {

    @Inject
    OrgStaffService orgStaffService;

    @Override
    public RpcResult<List<OrgEmployee>> search(String query) {
        try {
            List<OrgEmployee> list = orgStaffService.search(query);
            return RpcResult.success(list);
        } catch (Exception e) {
            return RpcResult.failed(e);
        }
    }
}
