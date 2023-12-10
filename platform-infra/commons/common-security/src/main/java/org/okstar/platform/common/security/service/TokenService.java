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

package org.okstar.platform.common.security.service;

import io.smallrye.common.constraint.Assert;
import io.vertx.core.http.HttpServerRequest;
import org.okstar.platform.common.core.constant.CacheConstants;
import org.okstar.platform.common.core.constant.Constants;
import org.okstar.platform.common.core.constant.SecurityConstants;
import org.okstar.platform.common.core.utils.IdUtils;
import org.okstar.platform.common.core.utils.OkStringUtil;
import org.okstar.platform.common.core.utils.OkWebUtil;
import org.okstar.platform.common.redis.service.RedisService;
import org.okstar.platform.common.security.domain.LoginUser;
import org.okstar.platform.common.security.utils.SecurityUtils;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * token验证处理
 */
@Singleton
public class TokenService {

    //	@Value("${tokenVerifier.url:}")
    private String url;
    //	@Value("${tokenVerifier.realm:}")
    private String realm;
    //	@Value("${tokenVerifier.clientId:}")
    private String clientId;
    //	@Value("${tokenVerifier.enableSSO:}")
    private String enableSSO;
    //    @Inject
    private RedisService redisService;

    private final static long EXPIRE_TIME = Constants.TOKEN_EXPIRE * 60;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;
    private final static String SSO_KEYCLOAK = "keycloak_subject:";

    protected static final long MILLIS_SECOND = 1000;

    /**
     * 设置过期时间
     */
    public void updateExpireTime(String token) {
//        if (enableSSO.equals("true")) {
//            String subject = extractedSubjectInToken(token);
//            redisService.expire(getSSOTokenKey(subject), TokenService.EXPIRE_TIME);
//        } else {
        redisService.expire(getTokenKey(token), TokenService.EXPIRE_TIME);
//        }
    }

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(HttpServerRequest request, LoginUser loginUser) {
        // 生成token
        Assert.assertNotNull(loginUser.getUserid());
        Assert.assertNotNull(loginUser.getUsername());

        String token = IdUtils.makeUuid();
        loginUser.setToken(token);
        loginUser.setIpaddr(OkWebUtil.getIpAddr(request));
        refreshToken(loginUser);

        // 保存或更新用户token
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("access_token", token);
        map.put("expires_in", EXPIRE_TIME);
        redisService.setCacheObject(ACCESS_TOKEN + token, loginUser, EXPIRE_TIME, TimeUnit.SECONDS);
        return map;
    }


    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServerRequest request) {
        // 获取请求携带的令牌
        String token = OkWebUtil.getValue(request, SecurityConstants.TOKEN_AUTHENTICATION);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        if (OkStringUtil.isNotEmpty(token)) {

            String userKey = getTokenKey(token);
            LoginUser user = redisService.getCacheObject(userKey);
            return user;
        }
        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (!Objects.isNull(loginUser) && OkStringUtil.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    public void delLoginUser(String token) {
        if (OkStringUtil.isNotEmpty(token)) {
            String userKey = getTokenKey(token);
            redisService.deleteObject(userKey);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + EXPIRE_TIME * MILLIS_SECOND);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public void refreshTokenSSO(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + EXPIRE_TIME * MILLIS_SECOND);
        // 根据uuid将loginUser缓存
        String userKey = getSSOTokenKey(loginUser.getSubject());
        redisService.setCacheObject(userKey, loginUser, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }

    private String getSSOTokenKey(String subject) {
        return SSO_KEYCLOAK + subject;
    }

    public LoginUser getLoginUserFromSSO(String subject) {
        if (OkStringUtil.isNotEmpty(subject)) {
            String userKey = getSSOTokenKey(subject);
            LoginUser user = redisService.getCacheObject(userKey);
            return user;
        }
        return null;
    }


    public String getToken(HttpServerRequest request) {
        String token = request.getHeader(SecurityConstants.TOKEN_AUTHENTICATION);
        return SecurityUtils.replaceTokenPrefix(token);
    }


}
