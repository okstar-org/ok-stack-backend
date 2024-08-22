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
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import com.google.common.io.Files;
import com.obsidiandynamics.dockercompose.DockerCompose;
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.SneakyThrows;
import org.okstar.platform.common.core.exception.OkRuntimeException;
import org.okstar.platform.common.os.PortFinder;
import org.okstar.platform.common.string.OkStringUtil;
import org.okstar.platform.tenant.dto.InstanceRunningDTO;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Startup
@ApplicationScoped
public class DockerServiceImpl implements DockerService {

    YAMLFactory yamlFactory = new YAMLFactory();
    ObjectMapper yamlMapper = new ObjectMapper(yamlFactory);

    DockerClientConfig config;

    public void init(@Observes StartupEvent e) {
        config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        RemoteApiVersion apiVersion = config.getApiVersion();
        Log.infof("Docker apiVersion=%s", apiVersion);
        printVersion();
    }

    private DockerHttpClient createClient() {
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();
    }

    private void printVersion() {
        DockerClient dockerClient = DockerClientImpl.getInstance(config, createClient());
        var versionCmd = dockerClient.versionCmd();
        Log.infof("version=> %s", versionCmd.exec());
    }

    @Override
    public String findContainerByName(String name) {
        DockerClient dockerClient = DockerClientImpl.getInstance(config, createClient());
        var attached = dockerClient.listContainersCmd();
        var response = attached.withShowAll(true).exec();
        for (Container c : response) {
            if (OkStringUtil.containsAny("/" + name, c.getNames())) {
                return c.getId();
            }
        }
        return null;
    }

    @Override
    public Container getContainer(String containerId) {
        DockerClient dockerClient = DockerClientImpl.getInstance(config, createClient());
        var attached = dockerClient.listContainersCmd();
        var response = attached.withShowAll(true).exec();
        for (Container c : response) {
            if (OkStringUtil.equals(containerId, c.getId())) {
                return c;
            }
        }
        return null;
    }


    @SneakyThrows
    @Override
    public String createContainer(String name, String image, String port, String portBinds, String... env) {

        DockerClient dockerClient = DockerClientImpl.getInstance(config, createClient());
        PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
        try {
            pullImageCmd.start().awaitCompletion();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        CreateContainerCmd cmd = dockerClient.createContainerCmd(image);

        cmd.withName(name).withExposedPorts(ExposedPort.parse(port))
                .withPortBindings(PortBinding.parse(portBinds))
                .withEnv(env);

        CreateContainerResponse response = cmd.exec();
        Log.infof("create container[%s]=> %s", image, response.getId());
        dockerClient.close();
        return response.getId();
    }

    @Override
    public void startContainer(String containerId) {
        Log.infof("Starting container: %s", containerId);
        DockerClient dockerClient = DockerClientImpl.getInstance(config, createClient());
        StartContainerCmd started = dockerClient.startContainerCmd(containerId);
        started.exec();
        Log.infof("Started container[%s]", containerId);
    }

    @Override
    public void stopContainer(String containerId) {
        Log.infof("Stopping container: %s", containerId);
        Container container = getContainer(containerId);
        if (container == null) {
            Log.warnf("container not found: %s", containerId);
            return;
        }

        DockerClient dockerClient = DockerClientImpl.getInstance(config, createClient());
        StopContainerCmd stopped = dockerClient.stopContainerCmd(containerId);
        stopped.exec();
        Log.infof("Stopped container[%s]", containerId);
    }

    @Override
    public InstanceRunningDTO up(String yml, String uuid) {
        Log.infof("Up docker-compose:%s...", uuid);
        try {
            InstanceRunningDTO runningDTO = new InstanceRunningDTO();
            File ymlFile = filterYaml(yml, uuid, runningDTO);

            DockerCompose compose = new DockerCompose();
            compose.withComposeFile(ymlFile.toPath().toString());
            compose.up();

            Log.infof("Up docker-compose successfully");
            return runningDTO;
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            Log.errorf(cause, "Unable to up the docker compose!");
            throw new OkRuntimeException(cause.getMessage());
        }


    }


    @Override
    public boolean down(String yml, String uuid) {
        Log.infof("Down docker-compose...");
        try {
            File ymlFile = filterYaml(yml, uuid, null);
            DockerCompose compose = new DockerCompose();
            compose.withComposeFile(ymlFile.toPath().toString());
            compose.down(false);
            Log.infof("Down docker-compose successfully");
        } catch (Exception e) {
            Log.errorf(e, "Unable to up the docker compose!");
            return false;
        }

        return true;
    }

    private File makeYamlFile(String yml) throws IOException {
        var ymlContent = yml.replace("\\n", "\n");

        File ymlFile = File.createTempFile("docker-compose-" + OkStringUtil.makeRandom(10), ".yml");
        Log.infof("docker-compose:%s", ymlFile.getAbsolutePath());

        //写入文件
        Files.write(ymlContent.getBytes(StandardCharsets.UTF_8), ymlFile);
        return ymlFile;
    }


    @SneakyThrows
    private File filterYaml(String yml, String uuid, InstanceRunningDTO runningDTO) {
        //替换\\n->\n
        var ymlContent = yml.replace("\\n", "\n");
        //${HOME}/okstar/ok-stack/saas/tenant/instance/{实例uuid}
        String home = System.getenv().get("HOME");
        String volume = "%s/okstar/ok-stack/saas/tenant/instance/%s".formatted(home, uuid);
        Log.infof("volume:%s", volume);

        //yml file -> object
        yamlMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        DockerComposeYaml yaml = yamlMapper.readValue(ymlContent, DockerComposeYaml.class);
        yaml.getServices().forEach((s, v) -> {
            v.setContainer_name("tenant_%s_%s".formatted(uuid, v.getContainer_name()));
            for (int i = 0; i < v.getPorts().size(); i++) {
                var pair = v.getPorts().get(i);
                var p = pair.split(":");
                int port = PortFinder.findAvailablePort();
                String newPort = port + ":" + p[1];
                v.getPorts().set(i, newPort);
                if (runningDTO != null) {
                    runningDTO.addPort(newPort);
                }
            }
            for (int i = 0; i < v.getVolumes().size(); i++) {
                String pair = v.getVolumes().get(i);
                var p = pair.split(":");
                var nv = volume + ":" + p[1];
                v.getVolumes().set(i, nv);
                if (runningDTO != null) {
                    runningDTO.addVolume(nv);
                }
            }
        });

        File ymlFile = File.createTempFile("docker-compose-" + OkStringUtil.makeRandom(10), ".yml");
        Log.infof("docker-compose:%s", ymlFile.getAbsolutePath());
        yamlMapper.writeValue(ymlFile, yaml);

        return ymlFile;
    }
}
