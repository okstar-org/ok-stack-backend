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

package org.okstar.platform.common.datasource;

import io.agroal.api.AgroalDataSource;
import io.agroal.api.AgroalDataSourceMetrics;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Readiness
@ApplicationScoped
public class DataSourceHealthCheck implements HealthCheck {
    @Inject
    AgroalDataSource agroalDataSource;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Database connections health check");
        responseBuilder.up();


        StringBuilder stringBuilder = new StringBuilder();
        AgroalDataSourceMetrics metrics = agroalDataSource.getMetrics();
        stringBuilder.append("<pre>")
                .append("activeCount").append("=").append(metrics.activeCount()).append(";\n")
                .append("leakDetectionCount").append("=").append(metrics.leakDetectionCount()).append(";\n")
                .append("creationCount").append("=").append(metrics.creationCount()).append(";\n")
                .append("availableCount").append("=").append(metrics.availableCount()).append(";\n")
                .append("maxUsedCount").append("=").append(metrics.maxUsedCount()).append(";\n")
                .append("awaitingCount").append("=").append(metrics.awaitingCount()).append(";\n")
                .append("blockingTimeAverage").append("=").append(metrics.blockingTimeAverage().toSeconds()).append("s").append(";\n")
                .append("blockingTimeMax").append("=").append(metrics.blockingTimeMax().toSeconds()).append("s").append(";\n");
        stringBuilder.append("</pre>");
        responseBuilder.withData("OkStar", stringBuilder.toString());
        return responseBuilder.build();
    }
}
