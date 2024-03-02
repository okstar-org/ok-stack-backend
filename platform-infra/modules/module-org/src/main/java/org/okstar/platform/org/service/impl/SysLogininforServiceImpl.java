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

package org.okstar.platform.org.service.impl;


import jakarta.enterprise.context.ApplicationScoped;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.org.domain.SysLogininfor;
import org.okstar.platform.org.service.ISysLogininforService;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 * 
 * 
 */
@ApplicationScoped
public class SysLogininforServiceImpl implements ISysLogininforService
{

    @Override
    public void save(SysLogininfor sysLogininfor) {

    }

    @Override
    public List<SysLogininfor> findAll() {
        return null;
    }

    @Override
    public OkPageResult<SysLogininfor> findPage(OkPageable page) {
        return null;
    }

    @Override
    public SysLogininfor get(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(SysLogininfor sysLogininfor) {

    }
}
