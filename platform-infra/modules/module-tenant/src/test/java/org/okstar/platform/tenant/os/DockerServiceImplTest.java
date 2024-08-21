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

import de.gesellix.docker.compose.ComposeFileReader;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

@QuarkusTest
class DockerServiceImplTest {
    @Inject
    DockerService dockerService;

    @Test
    void createContainer() {
    }

    @Test
    void startContainer() {
        String id="f8969f164d7c476a051fdfa4925ad159c82edbcf58750090e0cbba454dad00ab";
        dockerService.startContainer(id);
    }

    @Test
    public void parseDockerCompose( ){
//        int localPort = PortFinder.findAvailablePort();
        String dc = "services:\n   appsmith:\n     image: index.docker.io/appsmith/appsmith-ce\n     container_name: appsmith\n     ports:\n         - 180:80\n         - 1443:443\n     volumes:\n         - ./stacks:/appsmith-stacks\n     restart: unless-stopped\n";
        ComposeFileReader composeFileReader = new ComposeFileReader();
        HashMap<String, Map<String, Map<String, Object>>> map
                = composeFileReader.loadYaml(new ByteArrayInputStream(dc.getBytes()));
        Log.infof("map=%s", map);
    }
}