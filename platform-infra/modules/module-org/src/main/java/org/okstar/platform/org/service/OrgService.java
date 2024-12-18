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

package org.okstar.platform.org.service;


import org.okstar.platform.common.datasource.OkJpaService;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.dto.Org0;

/**
 * 组织服务接口
 */
public interface OrgService extends OkJpaService<Org>
{
    Org loadCurrent();

    Org0 loadCurrent0();

    Org setDefault();

    void setCert(Long id, String cert);

    Org save(Org0 org0);
}
