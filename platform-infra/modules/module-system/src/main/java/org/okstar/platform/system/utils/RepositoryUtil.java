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

package org.okstar.platform.system.utils;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;

import java.util.Map;

public class RepositoryUtil {

    public static  <T> PanacheQuery<T> queryOfExample(PanacheRepository<T> repo, T user) {
        Map<String, Object> map = OkBeanUtils.toMap(user);
        PanacheQuery<T> p = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if (v != null) {
                p = repo.find(k, v);
            }
        }
        if(p == null){
            p= repo.findAll();
        }
        return p;
    }

}
