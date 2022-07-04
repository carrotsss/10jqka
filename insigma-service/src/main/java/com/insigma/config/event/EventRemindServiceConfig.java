package com.insigma.event;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName EventRemindServiceConfig
 * @Description
 * @Author carrots
 * @Date 2022/6/21 11:32
 * @Version 1.0
 */
@Configuration
public class EventRemindServiceConfig {
    @Resource
    private EarningCallEventService earningCallEventService;

    private Map<String, EventRemindService> serviceMap = news ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        serviceMap.put(EventRemindEnum.EARINGS_CALL.getCode(), earningCallEventService);
    }

    public EventRemindService getEventRemindService(String eventType) {
        return serviceMap.get(eventType);
    }

}
