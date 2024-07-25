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

package org.okstar.platform.common.thread;

import org.okstar.platform.common.core.exception.OkRuntimeException;

import java.util.concurrent.TimeUnit;

public class OkThreadUtils {

    /**
     * 线程等待
     * @param sec 秒
     * @param quiet 是否静默，
     *   静默被打断直接返回，非静默抛出异常
     */
    public static void sleepSeconds(int sec, boolean quiet) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            if (quiet) {
                return;
            }
            throw new OkRuntimeException(e);
        }
    }
}
