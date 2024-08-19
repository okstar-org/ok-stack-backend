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

package org.okstar.platform.tenant.manager;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.system.vo.SysAccount0;
import org.okstar.platform.tenant.dto.TenantCreateDTO;

import java.util.concurrent.TimeUnit;

@QuarkusTest
class TenantManagerImplTest {

    @Inject
    TenantManager tenantManager;

    @Test
    void create() throws InterruptedException {
        TenantCreateDTO t = new TenantCreateDTO() ;
        t.setName("测试租户");
        t.setNo(OkIdUtils.makeUuid());
        SysAccount0 self = new SysAccount0();
        self.setId(1L);
        Long id = tenantManager.create(t, self);
        Log.infof("Create tenant id => %s", id);
        Assert.assertTrue(id > 0);
        TimeUnit.SECONDS.sleep(5);
    }

    @Test
    void testCreateResource() {
        tenantManager.createResource(701L);
    }


    @Test
    void testStartContainer(){
        String id = "ca98e7843515";
        tenantManager.startContainer(id);
    }
}