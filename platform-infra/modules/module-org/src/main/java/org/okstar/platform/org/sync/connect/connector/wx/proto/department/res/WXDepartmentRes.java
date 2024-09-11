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

package org.okstar.platform.org.sync.connect.connector.wx.proto.department.res;


import lombok.*;
import org.okstar.platform.org.sync.connect.connector.wx.proto.WXRes;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.proto.SysConnDepartment;

import java.util.List;
import java.util.stream.Collectors;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WXDepartmentRes extends WXRes<List<SysConnDepartment>> {

    List<DepartmentItem> department;

    @Override
    public List<SysConnDepartment> to(OrgIntegrateConf app) {
        return department.stream().map(d -> {

                    var x = SysConnDepartment.builder()
                            .parentId(String.valueOf(d.getParentid()))
                            .id(d.getId())
                            .name(d.getName())
                            .build();
                    x.setAppId(app.getAppId());
                    x.setType(app.getType());
                    return x;
                }
        ).collect(Collectors.toList());
    }

    /**
     * {
     * "errcode": 0,
     * "errmsg": "ok",
     * "department": [
     * {
     * "id": 2,
     * "name": "广州研发中心",
     * "name_en": "RDGZ",
     * "parentid": 1,
     * "order": 10
     * },
     * {
     * "id": 3,
     * "name": "邮箱产品部",
     * "name_en": "mail",
     * "parentid": 2,
     * "order": 40
     * }
     * ]
     * }
     */
    @Data
    public static class DepartmentItem {
        String id;
        String name;
        String name_en;
        Long parentid;
        Integer order;
    }
}
