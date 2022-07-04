package com.insigma.handler.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.insigma.feign.HealthFeign;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @ClassName CacheLoader
 * @Description
 * @Author carrots
 * @Date 2022/6/20 16:07
 * @Version 1.0
 */
@Component
public class CaEventLoader<T, R, N>
        extends OvseLocalCache<T, Object>
{
    private static final long CACHE_MAX_SIZE = 500L;

    private static final long CACHE_DURATION = 90L;
    private static final int EVENT_PAGE = 0;
    private static final int PAGE_SIZE = 50;

    //这个应该在OvseLocalCache里初始化的
    Cache cache = null;

    @Autowired
    HealthFeign healthFeign;

    public CaEventLoader() {
        this.cache = Caffeine.newBuilder()
                .maximumSize(CACHE_MAX_SIZE)
                .expireAfterWrite(CACHE_DURATION, TimeUnit.DAYS)
                .build();
    }

    public Object get(String key) {
        String secuId = key.split(":")[0];
        int startTime = Integer.parseInt(key.split(":")[1]);
        int endTime = Integer.parseInt(key.split(":")[1 << 1]);
        Object event = null;
        try {
            event = this.cache.get(key, new Function() {
                        @Override
                        public Object apply(Object o) {
                            return healthFeign.getListByVersion(secuId);
                        }
                    }
//                    k -> healthFeign.getListByVersion(secuId)
            );
        } catch (Exception e) {
//            OvseStatLog.error(XXXCONSTANT.SUCCESS_NAME, YYYCONST.SUCCESS_MSG, JavaDispatcher.Instance, e);
//            OvseAlarm.alarm("title", "获取参数为" + key, XXXCONSTANT);
        }
        return event;
    }
}
