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

package org.okstar.platform.org.sync.connect.connector.feishu.proto.access.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.okstar.platform.org.sync.connect.connector.feishu.proto.FSRes;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.proto.SysConnAccessToken;


@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FSAccessTokenRes extends FSRes<SysConnAccessToken> {

    @JsonProperty("app_access_token")
    String appAccessToken;
    @JsonProperty("expire")
    Long expire;

    public SysConnAccessToken to(OrgIntegrateConf app) {

        var x = SysConnAccessToken.builder()
                .accessToken(this.getAppAccessToken())
                .expiresIn(this.getExpire())
                .build();

        x.setType(app.getType());
        x.setAppId(app.getAppId());

        return x;
    }

}
