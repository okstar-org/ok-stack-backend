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

package org.okstar.platform.common.datasource;

import org.okstar.platform.common.core.utils.OkDateUtils;
import org.okstar.platform.common.core.utils.bean.OkBeanUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.datasource.domain.OkEntity;

import java.util.List;

public interface OkService <T extends OkEntity> {

    default void update(T t, Long updateBy){
        T exist = get(t.id);
        OkBeanUtils.copyPropertiesTo(t, exist);
        exist.setUpdateAt(OkDateUtils.now());
        exist.setUpdateBy(updateBy);
        save(exist);
    }

    default void create(T t, Long createBy){
        t.id = null;
        t.setCreateAt(OkDateUtils.now());
        t.setUpdateBy(createBy);
        save(t);
    }

    void save(T t);

    List<T> findAll();

    OkPageResult<T> findPage( OkPageable page);

    T get(Long id);

    void deleteById(Long id);

    void delete(T t);
}
