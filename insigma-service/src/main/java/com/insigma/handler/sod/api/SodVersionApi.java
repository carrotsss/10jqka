package com.insigma.handler.sod.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class SodVersionApi extends RedisSingleOvseInnerCachedApi<SodVersion> {
    public static String BIZ_NAME = "SOD_VERSION";
    public static String CACHE_KEY = "SOD_VERSION_CACHE";

    @Autowired
    public SodVersionApi(StringRedisTemplate stringRedisTemplate, SodVersionLoader sodVersionLoader) {
        super(BIZ_NAME, sodVersionLoader, stringRedisTemplate);
    }

    @Override
    public String getRedisCacheKey() {
        return CACHE_KEY;
    }

    public SodVersion getVersion() {
        return this.get();
    }


}
