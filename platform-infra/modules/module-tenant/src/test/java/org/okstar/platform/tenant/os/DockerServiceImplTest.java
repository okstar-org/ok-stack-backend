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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.gesellix.docker.compose.ComposeFileReader;
import de.gesellix.docker.compose.types.StackService;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.common.constraint.Assert;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.okstar.platform.common.os.PortFinder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        String id = "f8969f164d7c476a051fdfa4925ad159c82edbcf58750090e0cbba454dad00ab";
        dockerService.startContainer(id);
    }

    @Test
    public void parseDockerCompose() {
//        int localPort = PortFinder.findAvailablePort();
        String dc = "services:\n   appsmith:\n     image: index.docker.io/appsmith/appsmith-ce\n     container_name: appsmith\n     ports:\n         - 180:80\n         - 1443:443\n     volumes:\n         - ./stacks:/appsmith-stacks\n     restart: unless-stopped\n";
        ComposeFileReader composeFileReader = new ComposeFileReader();
        var map = composeFileReader.load(new ByteArrayInputStream(dc.getBytes()), "", System.getenv());
        Log.infof("map=%s", map);
        Log.infof("version=%s", map.getVersion());
        Map<String, StackService> services = map.getServices();
        services.forEach((k, v) -> {
            Log.infof("service=%s", k);
            Log.infof("image=%s", v.getImage());
            v.getPorts().getPortConfigs().forEach(p -> Log.infof("port=%s:%s/%s",
                    p.getPublished(), p.getTarget(), p.getProtocol()));
        });
        String string = map.toString();
        Log.infof("string=%s", string);
    }

    public void upDockerCompose() {
        String yml = "services:\n   appsmith:\n     image: index.docker.io/appsmith/appsmith-ce\n     container_name: appsmith\n     ports:\n         - 80:80\n         - 443:443\n     volumes:\n         - ./stacks:/appsmith-stacks\n     restart: unless-stopped\n";
        dockerService.up(yml, "TEST_UUID");
    }

    @Test
    public void useJackYamlParseDockerCompose() throws IOException {

        Map<String, String> getenv = System.getenv();
        Log.infof("env=%s", getenv);

        String yml = "services:\n   appsmith:\n     image: index.docker.io/appsmith/appsmith-ce\n     container_name: appsmith\n     ports:\n         - 80:80\n         - 443:443\n     volumes:\n         - ./stacks:/appsmith-stacks\n     restart: unless-stopped\n";

        YAMLFactory yamlFactory = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper(yamlFactory);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        DockerComposeYaml yaml = mapper.readValue(yml, DockerComposeYaml.class);
        Log.infof("yaml=%s", yaml);

        yaml.getServices().forEach((s, v) -> {
            for (int i = 0; i < v.getPorts().size(); i++) {
                var pair = v.getPorts().get(i);
                var p = pair.split(":");
                int port = PortFinder.findAvailablePort();
                v.getPorts().set(i, port + ":" + p[1]);
            }
        });
        mapper.writeValue(System.out, yaml);
        Assert.assertNotNull(yaml);
    }
}