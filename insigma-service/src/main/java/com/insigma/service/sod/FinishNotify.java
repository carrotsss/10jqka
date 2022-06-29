package com.insigma.service.sod;

import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName FinishNotify
 * @Description
 * @Author carrots
 * @Date 2022/6/29 14:18
 * @Version 1.0
 */
@Component
public class FinishNotify {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static long LOCK_TIMEOUT = 10;

    public void finishNotify() {
        RLock downloadLock = redissonClient.getLock(ApexSodServiceImpl.FINISH_NOTIFY_LOCK);
        RLock saveLock = redissonClient.getLock(ApexSodServiceImpl.FINISH_NOTIFY_LOCK);

        try {
            downloadLock.lock(LOCK_TIMEOUT, TimeUnit.SECONDS);
            saveLock.lock(LOCK_TIMEOUT, TimeUnit.SECONDS);
            Object downloadCache = redisTemplate.opsForValue().get(ApexSodServiceImpl.FINISH_NOTIFY_CACHE);
            Object saveCache = redisTemplate.opsForValue().get(SodSaveServiceImpl.FINISH_NOTITY_CACHE);
            String message = StringUtils.EMPTY;
            if (downloadCache != null) {
                message += "\n\t<下载>" + downloadCache.toString();
            }
            if (saveCache != null) {

                message += "\n\t<落库>" + saveCache.toString();
            }
            redisTemplate.delete(ApexSodServiceImpl.FINISH_NOTIFY_CACHE);
            redisTemplate.delete(sodSaveServiceImpl.FINISH_NOTIFY_CACHE);
            if (StringUtils.isNotBlank(message)) {
                VanishPusher.push(message);
            }
        }finally {
            try {
                downloadLock.unlock();
            } catch (Exception e) {
            }
            try {
                saveLock.unlock();
            } catch (Exception e) {
            }
        }
    }
}
