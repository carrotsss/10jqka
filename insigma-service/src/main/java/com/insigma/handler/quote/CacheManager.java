package com.insigma.handler.quote;

import com.insigma.handler.quote.command.CommandWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

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
    private CacheTasks cacheTask;

    @Resource
    private ThreadPoolTaskExecutor executor;

    @Value("${quote.redis.queue.swith:true}")
    private Boolean queueSwith;

    @Value("${quote.redis.task.pullsize:4000}")
    private Integer pullSize;

    @Value("${quote.redis.size:20000}")
    private Integer queueSize;

    private BlockingQueue<CommandWrapper> cacheQueue;

    public void handle(List<CommandWrapper> commandWrappers) {
        if (CollectionUtils.isEmpty(commandWrappers)) {
            return;
        }
        if (!queueSwith) {
            executor.execute(() -> {
                cacheTask.invoke(commandWrappers);
            });
            return;
        }
        commandWrappers.forEach(o -> {
            try {
                cacheQueue.put(o);
            } catch (InterruptedException e) {
                //统一处理告警
            }
        });
    }

    @PostConstruct
    public void initQueue() {
        if (!queueSwith) {
            return;
        }
        cacheQueue = new LinkedBlockingQueue<>(queueSize);
        new Thread(() -> {
            for (; ; ) {
                List<CommandWrapper> commandWrappers = new LinkedList<>();
                for (int i = 0; i < pullSize; i++) {
                    CommandWrapper commandWrapper = null;
                    try {
                        commandWrapper = cacheQueue.poll(100L, TimeUnit.MILLISECONDS);
                    } catch (Exception e) {

                    }
                    if (commandWrapper == null) {
                        break;
                    }
                    commandWrappers.add(commandWrapper);
                }
                if (CollectionUtils.isEmpty(commandWrappers)) {
                    executor.execute(() -> {
                        cacheTask.invoke(commandWrappers);
                    });
                }
            }
        }).start();
    }
}
