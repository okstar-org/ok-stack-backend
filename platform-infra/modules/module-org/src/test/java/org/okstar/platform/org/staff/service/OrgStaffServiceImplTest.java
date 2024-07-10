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

package org.okstar.platform.org.staff.service;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.org.domain.OrgStaff;
import org.okstar.platform.org.vo.OrgStaffFind;

@QuarkusTest
class OrgStaffServiceImplTest {

    @Inject OrgStaffService orgStaffService;


    @Test
    void findPage() {
        OrgStaffFind find = new OrgStaffFind();
        find.setPageIndex(0);
        find.setPageSize(20);
        find.setDeptId(601L);

        OkPageResult<OrgStaff> page = orgStaffService.findPage(find);
        for (OrgStaff staff : page.getList()) {

            Log.infof("staff:%s", staff);
        }

    }
}