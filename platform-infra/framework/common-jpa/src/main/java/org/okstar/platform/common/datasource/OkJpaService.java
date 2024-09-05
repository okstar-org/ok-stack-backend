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

package org.okstar.platform.common.datasource;

import jakarta.transaction.Transactional;
import org.okstar.platform.common.bean.OkBeanUtils;
import org.okstar.platform.common.core.web.page.OkPageResult;
import org.okstar.platform.common.core.web.page.OkPageable;
import org.okstar.platform.common.datasource.domain.OkEntity;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.core.service.OkService;

import java.util.List;

/**
 * JPA 存储通用服务
 * @param <T>
 */
@Transactional
public interface OkJpaService<T extends OkEntity> extends OkService {

    default void update(T t, Long updateBy){
        T exist = get(t.id);
        OkBeanUtils.copyPropertiesTo(t, exist);
        exist.setUpdateBy(updateBy);
        if (exist.getUpdateAt() == null)
            exist.setUpdateAt(OkDateUtils.now());
        if (t.getUuid() == null) {
            t.setUuid(OkIdUtils.makeUuid());
        }
        save(exist);
    }

    /**
     *
     * @param t
     * @param createBy
     */
    default void create(T t, Long createBy){
        t.id = null;
        t.setCreateBy(createBy);
        if (t.getCreateAt() == null) {
            t.setCreateAt(OkDateUtils.now());
        }
        if (t.getUuid() == null) {
            t.setUuid(OkIdUtils.makeUuid());
        }

        save(t);
    }

    void save(T t);

    List<T> findAll();

    OkPageResult<T> findPage(OkPageable page);

    T get(Long id);

    void deleteById(Long id);

    void delete(T t);

    /**
     * find by uuid
     * @param uuid
     * @return T
     */
    T get(String uuid);
}
