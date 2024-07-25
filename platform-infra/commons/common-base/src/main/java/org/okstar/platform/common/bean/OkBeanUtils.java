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

package org.okstar.platform.common.bean;


import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;


/**
 * Bean 工具类
 */
public class OkBeanUtils extends BeanUtils {


    public static void copyPropertiesTo(Object src, Object to) {
        if (src == null)
            return;
        try {
            copyProperties(to, src);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public static <T> Map<String, Object> toMap(T t) {
        Map<String, Object> map = new HashMap<>();
        Object object = t.getClass().newInstance();
        Field[] fields = FieldUtils.getAllFields(t.getClass());
        for (int i = 0; i < fields.length; i++) {
            map.put(fields[i].getName(), fields[i].get(object));
        }

        return map;
    }
}
