package com.insigma.config.thread;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @ClassName ThreadPoolTaskProperties
 * @Description
 * @Author carrots
 * @Date 2022/7/4 10:38
 * @Version 1.0
 */
@Component
@Builder
@Data
public class ThreadPoolTaskProperties {
    private Integer corePoolSize = 5;
    private Integer maxPoolSize = 10;
    private Integer queueCapcity = Integer.MAX_VALUE;
    private boolean alLowCoreThreadTimeOut = false;

}
