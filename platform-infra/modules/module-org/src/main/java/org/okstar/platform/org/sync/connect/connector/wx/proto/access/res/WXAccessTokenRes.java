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

package org.okstar.platform.org.sync.connect.connector.wx.proto.access.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.okstar.platform.org.sync.connect.connector.wx.proto.WXRes;
import org.okstar.platform.org.sync.connect.domain.OrgIntegrateConf;
import org.okstar.platform.org.sync.connect.proto.SysConnAccessToken;

/**
 * "access_token": "accesstoken000001",
 * "expires_in": 7200
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WXAccessTokenRes extends WXRes<SysConnAccessToken> {

    @JsonProperty("access_token")
    String accessToken;

    @JsonProperty("expires_in")
    Long expireIn;

    public SysConnAccessToken to(OrgIntegrateConf app) {
        SysConnAccessToken r = SysConnAccessToken.builder()
                .accessToken(this.getAccessToken())
                .expiresIn(this.getExpireIn())
                .build();
        r.setType(getType());
        r.setAppId(String.valueOf(app.getAppId()));
        return r;
    }
}
