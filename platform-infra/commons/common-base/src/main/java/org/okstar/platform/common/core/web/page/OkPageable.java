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

import lombok.*;
import org.okstar.platform.common.core.web.bean.VO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OkPageable extends VO {

    int pageIndex;

    int pageSize;

    public static OkPageable of(int index, int size) {
        return new OkPageable(index, size);
    }

    public int getPageIndex() {
        return pageIndex >= 0 ? pageIndex : 0;
    }

    public int getPageSize() {
        return pageSize <= 0 || pageSize > 1024 ? 10 : pageSize;
    }
}
