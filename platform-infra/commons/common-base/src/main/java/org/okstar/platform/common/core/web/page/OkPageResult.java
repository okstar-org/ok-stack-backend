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

package org.okstar.platform.common.core.web.page;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OkPageResult<T> implements Serializable {


    /**
     * 总记录数
     */
    long totalCount;

    /**
     * 总页数
     */
    long pageCount;

    /**
     * 列表数据
     */
    List<T> list;

    public static <T> OkPageResult<T> build(List<T> list,
                                            long totalCount,
                                            long pageCount) {
        OkPageResult<T> t = new OkPageResult<>();
        t.setTotalCount(totalCount);
        t.setPageCount(pageCount);
        t.setList(list);
        return t;
    }
}
