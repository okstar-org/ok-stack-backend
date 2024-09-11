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

package org.okstar.platform.org.sync.connect;


public interface SysConAppClientService {

    /**
     * 测试方法
     *
     * @param type
     * @return
     */
    String test(SysConEnums.SysConType type);

    /**
     * 同步方法
     *
     * @param type
     * @return
     */
    Boolean sync(SysConEnums.SysConType type);

    /**
     * 同步人员
     *
     * @param type
     * @return
     */
    Boolean syncUser(SysConEnums.SysConType type);
}
