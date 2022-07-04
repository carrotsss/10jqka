package com.insigma.config.thread;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @ClassName TaskAysncExecutorConfig
 * @Description
 * @Author carrots
 * @Date 2022/7/4 10:32
 * @Version 1.0
 */
@Configuration
@EnableAsync
public class TaskAysncExecutorConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
