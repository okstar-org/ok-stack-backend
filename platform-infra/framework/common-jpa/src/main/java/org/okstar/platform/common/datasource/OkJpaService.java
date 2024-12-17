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
import org.okstar.platform.common.datasource.domain.OkEntity;
import org.okstar.platform.common.date.OkDateUtils;
import org.okstar.platform.common.id.OkIdUtils;
import org.okstar.platform.common.web.page.OkPageResult;
import org.okstar.platform.common.web.page.OkPageable;
import org.okstar.platform.core.service.OkService;

import java.util.List;

/**
 * JPA 存储通用服务
 * @param <T>
 */
@Transactional
public interface OkJpaService<T extends OkEntity> extends OkService {

    /**
     * 更新实体
     * @param t
     * @param updateBy
     */
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
     * 创建实体
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

    /**
     * 保存实体接口，具体由各自子类实现
     * @param t
     */
    void save(T t);

    /**
     * 查询所有
     * @return
     */
    List<T> findAll();

    /**
     * 查询分页
     * @param page
     * @return
     */
    OkPageResult<T> findPage(OkPageable page);

    /**
     * 通过ID查询单个
     * @param id
     * @return
     */
    T get(Long id);

    /**
     * 通过ID删除实体
     * @param id
     */
    void deleteById(Long id);

    /**
     * 删除实体
     * @param t
     */
    void delete(T t);

    /**
     * 通过 uuid 查询实体
     * @param uuid 具有uuid的实体
     * @return T
     * @see OkEntity#uuid
     */
    T get(String uuid);
}
