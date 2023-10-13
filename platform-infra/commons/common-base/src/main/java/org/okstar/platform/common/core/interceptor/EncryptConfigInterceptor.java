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

package org.okstar.platform.common.core.interceptor;

import io.quarkus.arc.Priority;
import io.smallrye.config.ConfigSourceInterceptor;
import io.smallrye.config.ConfigSourceInterceptorContext;
import io.smallrye.config.ConfigValue;
import io.smallrye.config.Priorities;
import org.okstar.platform.common.security.AesEncryptUtil;

/**
 * 参考：
 * http://www.kailing.pub/article/index/arcid/289.html
 */

@Priority(value = Priorities.PLATFORM)//value 值越低优先级越高
public class EncryptConfigInterceptor implements ConfigSourceInterceptor {


    /**
     * 需要加密值的前缀标记
     */
    private static final String ENCRYPT_PREFIX_NAME = "Encrypt=>";

    /**
     * AES的iv向量值
     */
    private static final String AES_IV = "123456789okstar.";

    /**
     * AES KEY
     */
    private static final String AES_KEY = ".okstar.org(.cn)";

    @Override
    public ConfigValue getValue(ConfigSourceInterceptorContext context, String name) {
        ConfigValue config = context.proceed(name);
        if (config != null && config.getValue().startsWith(ENCRYPT_PREFIX_NAME)) {
            String encryptValue = config.getValue().replace(ENCRYPT_PREFIX_NAME, "");
            String value = AesEncryptUtil.decrypt(encryptValue, AES_KEY, AES_IV);
            return config.withValue(value);
        }
        return config;
    }



}
