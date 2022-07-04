package com.insigma.config.thread;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName AbstractThreadPoolFactory
 * @Description
 * @Author carrots
 * @Date 2022/7/4 10:22
 * @Version 1.0
 */
public class AbstractThreadPoolFactory {
    public TaskExecutor getThreadPool(ThreadPoolTaskProperties threadPoolTaskProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        if (threadPoolTaskProperties == null) {
            return executor;
        }
        executor.setAllowCoreThreadTimeOut(threadPoolTaskProperties.isAllowCoreThreadTimeOut());
        executor.setCorePoolSize(threadPoolTaskProperties.getCorePoolSize());
        executor.setKeepAliveSeconds(threadPoolTaskProperties.getKeepAliveSeconds());
        executor.setMaxPoolSize(threadPoolTaskProperties.getMaxPoolSize());
        if (threadPoolTaskProperties.getCorePoolSize() >= threadPoolTaskProperties.getMaxPoolSize) {
            executor.setMaxPoolSize(threadPoolTaskProperties.getCorePoolSize());
        }

        executor.setQueueCapacity(threadPoolTaskProperties.getQueueCapcity());
        /*线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy:默认为后者
        AbortPolicy：直接抛出RejectedExucutionException
        CallerRunsPolicy:之县城直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低线程池内部添加任务的速度
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        return executor;
    }
}
