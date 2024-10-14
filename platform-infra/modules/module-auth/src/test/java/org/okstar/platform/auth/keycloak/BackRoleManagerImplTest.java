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

package org.okstar.platform.auth.keycloak;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.common.web.page.OkPageable;

import java.util.List;

@QuarkusTest
class BackRoleManagerImplTest {
    @Inject BackRoleManager backRoleManager;

    @Test
    void page(){
        OkPageable p = new OkPageable(0, 10);
        List<BackRoleDTO> list = backRoleManager.page(p);
        list.forEach(role -> Log.infof("role=%s", role));
    }

    @Test
    void list() {
        List<BackRoleDTO> list = backRoleManager.list();
        list.forEach(role -> Log.infof("role=%s", role));
    }
}