package com.insigma.handler;

import com.insigma.handler.command.CommandWrapper;
import lombok.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;

/**
 * @ClassName CacheManager
 * @Description
 * @Author carrots
 * @Date 2022/6/23 16:56
 * @Version 1.0
 */
@Component
public class CacheManager {
    @Resource
    private CacheTask cacheTask;

    @Resource
    private ThreadPoolTaskExecutor executor;

    @Value("${quote.redis.queue.swith:true}")
    private Boolean queueSwith;

    @Value("${quote.redis.task.pullsize:4000}")
    private Integer pullSize;

    @Value("${quote.redis.size:20000}")
    private Integer queueSize;

    private BlockingQueue<CommandWrapper> cacheQueue;


}
