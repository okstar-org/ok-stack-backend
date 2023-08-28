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

package org.okstar.platform.system.resource;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.okstar.platform.common.core.web.bean.Res;
import org.okstar.platform.system.domain.SysUser;

import javax.inject.Inject;
import java.util.List;

/**
 * Controller单元测试
 */
@QuarkusTest
public class SysUserResourceTest {

    @Inject
    SysUserResource sysUserResource;

    @Test
    void testFindAll() {
        Res<List<SysUser>> all = sysUserResource.findAll();
        all.getData().forEach(user -> {
            Log.infof("user:%s", user.getUsername());
        });
    }
}
