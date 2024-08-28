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

package org.okstar.platform.system.account.service;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.system.dto.SysAccountDTO;

import java.util.List;

@QuarkusTest
class SysUserSearchServiceImplTest {

    @Inject
    SysUserSearchService sysUserSearchService;

    @Test
    void search() {
        String s = "杰";
        List<SysAccountDTO> list = sysUserSearchService.search(s);
        Assert.assertNotNull(list);
        Log.infof("search%s=>%d", s, list.size());
    }
}