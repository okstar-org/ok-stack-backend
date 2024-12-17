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

package org.okstar.platform.common.security.utils;

import io.quarkus.logging.Log;
import jakarta.json.JsonObject;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.okstar.platform.common.security.domain.OkAuthorization;
import org.okstar.platform.common.security.domain.OkLogonUser;
import org.okstar.platform.common.security.domain.OkRealmAccess;
import org.okstar.platform.common.security.domain.OkResourceAccess;

import java.time.Instant;

/**
 * Jwt工具类
 */
public class JwtUtils {

    /**
     * 从Jwt token解析 用户信息
     * @param jwt
     * @return OkLogonUser 登录用户
     */
    public static OkLogonUser parse(JsonWebToken jwt) {
        Log.infof("Parse jwt: %s", jwt);
        /**
         * DefaultJWTCallerPrincipal{
         * id='64d79e09-1d81-4790-a39d-c7da4ecbcd1b',
         * name='azpm4siwxbjg',
         * expiration=1729361308,
         * notBefore=0,
         * issuedAt=1729325309,
         * issuer='https://kc.okstar.org.cn/realms/ok-star',
         * audience=[ok-stack],
         * subject='0832a853-db89-467a-87c0-42b77293925c',
         * type='JWT',
         * issuedFor='ok-stack',
         * authTime=0, givenName='', familyName='', middleName='null', nickName='null', preferredUsername='azpm4siwxbjg',
         * email='18910221510@wo.cn',
         * emailVerified=true,
         * allowedOrigins=null,
         * updatedAt=0, acr='1', groups=[]}
         */
        OkLogonUser user = new OkLogonUser();

        user.setId(jwt.getTokenID());
        user.setUsername(jwt.getName());
        user.setSubject(jwt.getSubject());

        for (String claimName : jwt.getClaimNames()) {
            Object claim = jwt.getClaim(claimName);
            Log.infof("claimName:%s=>%s", claimName, claim);

            switch (claimName) {
                case "raw_token":
                    user.setToken((String) claim);
                    break;
                case "typ":
                    user.setType(claim.toString());
                    break;
                case "scope":
                    user.setScope(claim.toString());
                    break;
                case "given_name":
                    user.setGivenName(claim.toString());
                    break;
                case "middle_name":
                    user.setMiddleName(claim.toString());
                    break;
                case "family_name":
                    user.setFamilyName(claim.toString());
                    break;
                case "nick_name":
                    user.setNickName(claim.toString());
                    break;
                case "sid":
                    user.setSid(String.valueOf(claim));
                    break;
                case "email":
                    user.setEmail(String.valueOf(claim));
                    break;
                case "email_verified":
                    user.setEmailVerified(Boolean.valueOf(String.valueOf(claim)));
                    break;
                case "azp":
                    user.setClientId(String.valueOf(claim));
                    break;
                case "exp":
                    user.setExp(Instant.ofEpochSecond((Long) claim));
                    break;
                case "iat":
                    user.setIat(Instant.ofEpochSecond((Long) claim));
                    break;
                case "authorization":
                    user.setAuthorization(OkAuthorization.of((JsonObject) claim));
                    break;
                case "realm_access":
                    OkRealmAccess access = OkRealmAccess.of((JsonObject) claim);
                    user.setRealmAccess(access);
                    break;
                case "resource_access":
                    OkResourceAccess resourceAccess = OkResourceAccess.of((JsonObject) claim);
                    user.setResourceAccess(resourceAccess);
                    break;
                default:
                    break;
            }
        }
        user.setSubject(jwt.getSubject());
        return user;
    }
}
