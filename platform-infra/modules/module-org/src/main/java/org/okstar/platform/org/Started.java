/*
 * * Copyright (c) 2022 船山科技 chuanshantech.com
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

package org.okstar.platform.org;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.okstar.cloud.OkCloudApiClient;
import org.okstar.cloud.channel.FederalChannel;
import org.okstar.cloud.entity.AuthenticationToken;
import org.okstar.cloud.entity.FederalStateEntity;
import org.okstar.platform.org.domain.Org;
import org.okstar.platform.org.service.OrgService;

import java.util.Optional;
import java.util.concurrent.ExecutorService;

@ApplicationScoped
public class Started {

    OkCloudApiClient client;

    @Inject
    OrgService orgService;
    @Inject
    ExecutorService executorService;

    public Started() {
        client = new OkCloudApiClient("http://localhost:1024/open/stack",
                new AuthenticationToken("okstar", "okstar.123#"));
    }


    void init(@Observes StartupEvent e) {
        executorService.execute(this::setCert);
    }

    private void setCert() {
        Optional<Org> current = orgService.current();
        current.ifPresent(o -> {

            FederalStateEntity ex = new FederalStateEntity();
            ex.setName(o.getName());
            ex.setNo(o.getNo());

            FederalChannel channel = client.getFederalChannel();
            String cert = channel.ping(ex);
            Log.infof("Org cert=>%s", cert);
            if (cert != null) {
                orgService.setCert(o.id, cert);
            }
        });
    }


}
