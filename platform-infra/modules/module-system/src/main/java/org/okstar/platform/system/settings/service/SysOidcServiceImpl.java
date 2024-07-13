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

package org.okstar.platform.system.settings.service;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.okstar.platform.system.settings.domain.SysSetKv;
import org.okstar.platform.system.settings.mapper.SysSetKVMapper;

import java.util.List;

@Transactional
@ApplicationScoped
public class SysOidcServiceImpl implements SysOidcService {

    @Inject
    SysSetKVMapper kvMapper;

    void startup(@Observes StartupEvent event) {
        initOidc();
    }

    /**
     * # Configuration file
     *
     * @link https://quarkus.io/guides/security-oidc-configuration-properties-reference
     */
    @Override
    public void initOidc() {
        /**
         *       quarkus.oidc.client-id=okstack
         *       quarkus.oidc.auth-server-url=@quarkus.oidc.auth-server-url@
         *       quarkus.oidc.credentials.secret=@quarkus.oidc.credentials.secret@
         *       quarkus.oidc.tls.verification=none
         *
         */
        var group = "quarkus.oidc";

        long count = kvMapper.find("grouping", group).count();
        if(count > 0) {
            Log.warnf("Configurations of OIDC are exists. group: %s", group);
            return;
        }

        var oidcList = List.of(
                SysSetKv.builder()
                        .grouping(group)
                        .name("Client Id")
                        .k("quarkus.oidc.client-id")
                        .v("")
                        .comment("The client id of the application.")
                        .build(),
                SysSetKv.builder()
                        .grouping(group)
                        .name("Authorization Server Url")
                        .k("quarkus.oidc.auth-server-url")
                        .v("")
                        .comment("The base URL of the OpenID Connect (OIDC) server, for example, https://host:port/auth. ")
                        .build(),
                SysSetKv.builder()
                        .grouping(group)
                        .name("Client secret")
                        .k("quarkus.oidc.credentials.secret")
                        .v("")
                        .comment("The client secret.")
                        .build(),
                SysSetKv.builder()
                        .grouping(group)
                        .name("Https verification")
                        .k("quarkus.oidc.tls.verification")
                        .v("none")
                        .comment("Certificate validation and hostname verification.")
                        .build()
        );

        kvMapper.persist(oidcList);
    }

    /**
     * # Configuration file
     *
     * @link https://quarkus.io/guides/security-openid-connect-client-reference#oidc-token-propagation
     */
    @Override
    public void initOidcClient() {
        /**
         *       quarkus.oidc-client.auth-server-url=${quarkus.oidc.auth-server-url}
         *       quarkus.oidc-client.client-id=${quarkus.oidc.client-id}
         *       quarkus.oidc-client.credentials.secret=${quarkus.oidc.credentials.secret}
         *       quarkus.oidc-client.tls.verification=none
         *
         */
        var group = "quarkus.oidc-client";

        long count = kvMapper.find("grouping", group).count();
        if(count > 0) {
            Log.warnf("Configurations of OIDC are exists. group: %s", group);
            return;
        }

        var oidcClientList = List.of(
                SysSetKv.builder()
                        .grouping(group)
                        .name("Client Id")
                        .k("quarkus.oidc-client.client-id")
                        .v("")
                        .comment("The client id of the application.")
                        .build(),
                SysSetKv.builder()
                        .grouping(group)
                        .name("Authorization Server Url")
                        .k("quarkus.oidc-client.auth-server-url")
                        .v("")
                        .comment("The base URL of the OpenID Connect (OIDC) server, for example, https://host:port/auth. ")
                        .build(),
                SysSetKv.builder()
                        .grouping(group)
                        .name("Client secret")
                        .k("quarkus.oidc-client.credentials.secret")
                        .v("")
                        .comment("The client secret.")
                        .build(),
                SysSetKv.builder()
                        .grouping(group)
                        .name("Https verification")
                        .k("quarkus.oidc-client.tls.verification")
                        .v("none")
                        .comment("Certificate validation and hostname verification.")
                        .build()
        );

        kvMapper.persist(oidcClientList);
    }
}
