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

package org.okstar.platform.org.rpc.impl;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.common.rpc.RpcResult;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.dto.Org0;
import org.okstar.platform.org.rpc.OrgRpc;
import org.okstar.platform.org.service.OrgService;

import java.util.Optional;

@Transactional
@ApplicationScoped
public class OrgRpcImpl implements OrgRpc {

    @Inject
    OrgService orgService;

    @Override
    public RpcResult<Org0> current() {
        Optional<Org> org = orgService.current();
        if (org.isEmpty()) {
            return RpcResult.failed("Not exist");
        }
        Org0 o = new Org0();
        OkBeanUtils.copyPropertiesTo(org.get(), o);
        return RpcResult.success(o);
    }
}
