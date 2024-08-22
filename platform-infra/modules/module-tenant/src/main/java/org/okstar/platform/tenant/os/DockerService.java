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

package org.okstar.platform.tenant.os;

import com.github.dockerjava.api.model.Container;
import org.okstar.platform.tenant.dto.InstanceRunningDTO;

public interface DockerService {


    String findContainerByName(String name);

    Container getContainer(String containerId);

    String createContainer(String name, String image, String port, String portBinds, String... env);

    void startContainer(String containerId);

    void stopContainer(String containerId);

    InstanceRunningDTO up(String yml, String uuid);


    boolean down(String yml, String uuid);
}
