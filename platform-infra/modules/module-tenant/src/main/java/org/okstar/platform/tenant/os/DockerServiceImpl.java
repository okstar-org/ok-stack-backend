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
import io.quarkus.logging.Log;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import lombok.SneakyThrows;
import org.okstar.platform.common.string.OkStringUtil;

import java.time.Duration;

@Startup
@ApplicationScoped
public class DockerServiceImpl implements DockerService {

    DockerClientConfig config;
    DockerHttpClient client;


    public void init(@Observes StartupEvent e) {
        config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
        RemoteApiVersion apiVersion = config.getApiVersion();
        Log.infof("Docker apiVersion=%s", apiVersion);

        client = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        printVersion();

    }

    private void printVersion() {
        DockerClient dockerClient = DockerClientImpl.getInstance(config, client);
        var versionCmd = dockerClient.versionCmd();
        Log.infof("version=> %s", versionCmd.exec());
    }

    @Override
    public String findContainerByName(String name) {
        DockerClient dockerClient = DockerClientImpl.getInstance(config, client);
        var attached = dockerClient.listContainersCmd();
        var response = attached.withShowAll(true).exec();
        for (Container c : response) {
            if (OkStringUtil.containsAny("/"+name, c.getNames())) {
                return c.getId();
            }
        }
        return null;
    }

    @Override
    public Container getContainer(String containerId) {
        DockerClient dockerClient = DockerClientImpl.getInstance(config, client);
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

        DockerClient dockerClient = DockerClientImpl.getInstance(config, client);
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
        DockerClient dockerClient = DockerClientImpl.getInstance(config, client);
        StartContainerCmd started = dockerClient.startContainerCmd(containerId);
        started.exec();
        Log.infof("Started container[%s]", containerId);
    }

    @Override
    public void stopContainer(String containerId) {
        Log.infof("Stopping container: %s", containerId);
        DockerClient dockerClient = DockerClientImpl.getInstance(config, client);
        Container container = getContainer(containerId);
        if (container == null) {
            Log.warnf("container not found: %s", containerId);
            return;
        }

        StopContainerCmd stopped = dockerClient.stopContainerCmd(containerId);
        stopped.exec();
        Log.infof("Stopped container[%s]", containerId);
    }
}
