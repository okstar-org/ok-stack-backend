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

package org.okstar.platform.org.sync.connect.connector.feishu.proto.department.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.okstar.platform.org.sync.connect.connector.feishu.proto.FSRes;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.connect.api.Department;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 钉钉部门
 * https://open.feishu.cn/document/uAjLw4CM/ukTMukTMukTM/reference/contact-v3/department/list
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class FSDepartmentRes extends FSRes<List<Department>> {

    FSDepartmentData data;

    @Override
    public List<Department> to(OrgIntegrateConf app) {
        if(data.getItems() == null){
            return Collections.emptyList();
        }
        return data.getItems().stream()
                .map(e -> {
                            var x = Department.builder()   //
                                    .name(e.getName())  //
                                    .id(String.valueOf(e.getDepartmentId()))    //
                                    .parentId(String.valueOf(e.getParentDepartmentId()))
                                    .build();

                            return x;
                        }
                ).collect(Collectors.toList());
    }

    @Data
    public static class FSDepartmentData {
        @JsonProperty("has_more")
        boolean hasMore;
        @JsonProperty("page_token")
        String pageToken;

        List<FSDepartmentItem> items;
    }

    @Data
    public static class FSDepartmentItem {
        private String name;
        @JsonProperty("open_department_id")
        String departmentId;
        @JsonProperty("parent_department_id")
        String parentDepartmentId;
        @JsonProperty("member_count")
        Integer memberCount;
        Map<String, Object> status;
    }
}
