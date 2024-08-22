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

package org.okstar.platform.tenant.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 实例运行数据
 */
@Data
public class InstanceRunningDTO {
    List<String> ports;
    List<String> volumes;

    public void addPort(String newPort) {
        if (ports == null) {
            ports = new ArrayList<>();
        }
        ports.add(newPort);
    }

    public void addVolume(String nv) {
        if (volumes == null) {
            volumes = new ArrayList<>();
        }
        volumes.add(nv);
    }
}
