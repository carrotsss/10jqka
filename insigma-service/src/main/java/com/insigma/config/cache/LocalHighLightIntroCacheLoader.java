package com.insigma.config.cache;

import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @ClassName LocalHighLightIntroCacheLoader
 * @Description
 * @Author carrots
 * @Date 2022/7/1 9:46
 * @Version 1.0
 */
public class LocalHighLightIntroCacheLoader
        extends RedisDbDetetchedSetOvseApiCacheLoader<String, SharedHighLightDto>
        implements LocalHighLightIntroCache {
    public static final long CACHE_LOADER = 600L;
    private ShareHighLightService shareHighLightService;

    public LocalHighLightIntroCacheLoader(RedissonClient redissonClient, StringRedisTemplate stringRedisTemplate, String bizName, ShareHighLightService shareHighLightService) {
        super(redissonClient, stringRedisTemplate, bizName);
        this.shareHighLightService = shareHighLightService;
    }

    public void loadFromDb(String key) {

    }

    public void getCacheExpire() {

    }

}
