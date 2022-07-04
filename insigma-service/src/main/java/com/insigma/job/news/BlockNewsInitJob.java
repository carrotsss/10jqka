package com.insigma.job.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName BlockNewsInitJob
 * @Description
 * @Author carrots
 * @Date 2022/7/1 10:44
 * @Version 1.0
 */
@Component

public class BlockNewsInitJob {
    @Autowired
    @Qualifier("defaultStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    public void handle() {

    }
}
