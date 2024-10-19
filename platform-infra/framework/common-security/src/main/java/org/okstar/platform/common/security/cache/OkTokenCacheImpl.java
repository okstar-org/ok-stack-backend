
package org.okstar.platform.common.security.cache;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.okstar.platform.common.security.domain.OkLogonUser;
import org.okstar.platform.common.security.utils.JwtUtils;

import java.time.Duration;

@ApplicationScoped
public class OkTokenCacheImpl implements OkLogonUserCache {

    @Inject
    @CacheName(OkLogonUserCache.NAME)
    Cache cache;

    @Override
    @SneakyThrows
    public OkLogonUser get(String username) {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        Object o = caffeineCache.getIfPresent(username).get();
        if (o == null) {
            return null;
        }
        return (OkLogonUser) o;
    }

    @Override
    public OkLogonUser set(String username, JsonWebToken jsonWebToken) {
        return cache.get(username, (k) -> {
            OkLogonUser user = JwtUtils.parse(jsonWebToken);
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            caffeineCache.setExpireAfterWrite(Duration.between(user.getIat(), user.getExp()));
            Log.infof("Put logon user is: %s", user);
            return user;
        }).subscribe().asCompletionStage().toCompletableFuture().join();
    }

}