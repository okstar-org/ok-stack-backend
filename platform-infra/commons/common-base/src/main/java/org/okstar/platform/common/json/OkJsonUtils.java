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

package org.okstar.platform.common.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JacksonUtils;
import lombok.SneakyThrows;

/**
 * Json工具
 * 使用方法：
 * <code>
 *
 * @Inject OkJsonUtils jsonUtils
 * </code>
 */
public class OkJsonUtils {

    private OkJsonUtils(){}

    /**
     * 对象转JSON
     * @param object
     * @return
     */
    @SneakyThrows
    public static String asString(Object object) {
        ObjectMapper mapper = JacksonUtils.newMapper();
        return mapper.writeValueAsString(object);
    }

    /**
     * JSON转对象
     * @param json
     * @param clazz
     * @return
     * @param <T>
     */
    @SneakyThrows
    public static <T> T asObject(String json, Class<T> clazz) {
        ObjectMapper mapper = JacksonUtils.newMapper();
        return mapper.readValue(json, clazz);
    }
}
