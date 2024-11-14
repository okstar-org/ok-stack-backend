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

package org.okstar.platform.open.resource;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.okstar.platform.common.web.bean.Res;
import org.okstar.platform.core.rpc.RpcAssert;
import org.okstar.platform.org.dto.OrgEmployee;
import org.okstar.platform.org.rpc.OrgStaffRpc;

import java.time.Duration;
import java.util.List;
import java.util.Optional;


@Path("staff")
public class StaffResource {

    @Inject
    @RestClient
    OrgStaffRpc orgStaffRpc;

    @Inject
    @CacheName("ok-search-staff")
    Cache cache;

    @GET
    @Path("search")
    public Res<List<OrgEmployee>> search(@QueryParam("q") String query) {

        CaffeineCache cc = (CaffeineCache) cache;
        cc.setExpireAfterAccess(Duration.ofMinutes(10));

        String key = Optional.ofNullable(query).orElse("ALL");

        List<OrgEmployee> list = cache.get(key, (k) -> doSearch(key))
                .subscribe().asCompletionStage().toCompletableFuture().join();

        return Res.ok(list);
    }

    private List<OrgEmployee> doSearch(String query) {
        return RpcAssert.isTrue(orgStaffRpc.search(query));
    }
}
