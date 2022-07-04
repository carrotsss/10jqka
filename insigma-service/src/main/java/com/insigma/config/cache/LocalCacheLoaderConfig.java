package com.insigma.config.cache;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.security.Key;

/**
 * @ClassName LocalCacheLoaderConfig
 * @Description
 * @Author carrots
 * @Date 2022/6/30 16:58
 * @Version 1.0
 */
@Configuration
public class LocalCacheLoaderConfig {
    public static final String BIZ_NAME = "NEWS-F10";

    @Autowired
    @Qualifier("defaultStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private ShareIconService shareIconService;

    @Bean
    public HighlightIntroCacheLoader highlightIntroCacheLoader(ShardHighlightService shardHighlightService) {
        return new LocalHighlightIntroCacheLoader(redissonClient, stringRedisTemplate, BIZ_NAME + "HIGHLIGHTINTRO", shardHighlightService);
    }

    @Bean
    public ShareIconCacheLoader shareIconCacheLoader(ShareIconCacheLoader shareIconCacheLoader) {
        return new LocalShareIconCacheLoader(redissonClient, stringRedisTemplate, BIZ_NAME + "SHAREICON", shareIconService);
    }


    public String getHighLightIntroCacheKey(String key) {
        return "OVSE_CACHE_" + BIZ_NAME + "_" + key;
    }

    public String getShareIconCacheKey() {

    }

}
