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

package org.okstar.platform.org.sync.connect.service;

import org.okstar.platform.org.sync.connect.SysConEnums;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;

import java.util.List;

public class SysConAppServiceImpl implements SysConAppService {



    @Override
    public void save(OrgIntegrateConf orgIntegrateConf) {
        String appId = orgIntegrateConf.getAppId();

    }

    @Override
    public List<OrgIntegrateConf> findAll() {
        return List.of();
    }

    @Override
    public OrgIntegrateConf findOne(SysConEnums.SysConType type) {
        return null;
    }
}
