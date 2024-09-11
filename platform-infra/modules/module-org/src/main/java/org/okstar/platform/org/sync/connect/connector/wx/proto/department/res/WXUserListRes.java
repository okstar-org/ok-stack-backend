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

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.apache.commons.lang3.EnumUtils;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.core.user.UserDefines;
import org.okstar.platform.org.defined.StaffDefines;
import org.okstar.platform.org.sync.connect.connector.wx.proto.WXRes;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.dto.SysConUser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WXUserListRes extends WXRes<List<SysConUser>> {

    List<UserItem> userlist;

    @Override
    public List<SysConUser> to(OrgIntegrateConf app) {
        return userlist.stream().map(d -> {
                    var x = new SysConUser();
                    OkBeanUtils.copyPropertiesTo(d, x);
                    x.setUnionId(d.getOpen_userid());
                    x.setHiredDate(d.getHide_mobile());
                    x.setUserId(d.getUserid());
                    x.setActive(Objects.equals(d.getStatus(), Status.ACTIVE));
                    x.setGender(EnumUtils.getEnum(org.okstar.platform.core.user.UserDefines.Gender.class, d.getGender().name()));
                    x.setSource(StaffDefines.Source.WX);
                    x.setAppId(app.getAppId());
                    return x;
                }
        ).collect(Collectors.toList());
    }

    /**
     * "userid": "zhangsan",
     * "name": "李四",
     * "department": [1, 2],
     * "order": [1, 2],
     * "position": "后台工程师",
     * "mobile": "13800000000",
     * "gender": "1",
     * "email": "zhangsan@gzdev.com",
     * "is_leader_in_dept": [1, 0],
     * "avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/0",
     * "thumb_avatar": "http://wx.qlogo.cn/mmopen/ajNVdqHZLLA3WJ6DSZUfiakYe37PKnQhBIeOQBO4czqrnZDS79FH5Wm5m4X69TBicnHFlhiafvDwklOpZeXYQQ2icg/100",
     * "telephone": "020-123456",
     * "alias": "jackzhang",
     * "status": 1,
     * "address": "广州市海珠区新港中路",
     * "hide_mobile" : 0,
     * "english_name" : "jacky",
     * "open_userid": "xxxxxx",
     * "main_department": 1,
     */
    @Data
    public static class UserItem {
        private String userid;
        private String open_userid;
        private String name;
        private List<Long> department;
        private List<Long> order;
        private String position;
        private String mobile;
        private UserDefines.Gender gender;
        private String email;
        private List<Long> is_leader_in_dept;
        private String avatar;
        private String thumb_avatar;
        private String telephone;
        private String alias;
        private Status status;
        private String address;
        private String hide_mobile;
        private String english_name;
        private String main_department;
    }

    @Getter
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public enum Status {
        /**
         * 激活状态: 1=已激活，2=已禁用，4=未激活，5=退出企业。
         */
        NONE("未知"),
        ACTIVE("已激活"),
        DISABLED("已禁用"),
        UN_ACTIVE("未激活"),
        NONE2("未知"),
        EXITED("退出企业");

        private String text;

        Status(String text) {
            this.text = text;
        }


    }


}
