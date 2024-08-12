package org.okstar.platform.tenant;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * 多租户平台
 */
@QuarkusMain
public class ModuleTenantApplication {
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
