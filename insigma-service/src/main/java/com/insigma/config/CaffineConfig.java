package com.insigma.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @ClassName CaffineConfig
 * @Description
 * @Author carrots
 * @Date 2022/6/28 16:52
 * @Version 1.0
 */
@Component
public class CaffineConfig {
    @Bean("systemCommonCacheManager")
    public CacheManager systemCommonCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(2)));
        return cacheManager;
    }
}
