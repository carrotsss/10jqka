package com.insigma.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * @ClassName CaffineConfig
 * @Description
 * @Author carrots
 * @Date 2022/6/28 16:52
 * @Version 1.0
 */
@Configuration
public class CaffineConfig {

    private static final long MAXIMUM_SIZE = 200_000;
    @Bean("systemCommonCacheManager")
    public CacheManager systemCommonCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(2)));
        return cacheManager;
    }

    @Bean
    public Cache<String, List<ExchangeRateVO>> exchangeRateCache() {
        return Caffeine.newBuilder()
                .maximumSize(MAXIMUM_SIZE)
                .build();
    }
}
