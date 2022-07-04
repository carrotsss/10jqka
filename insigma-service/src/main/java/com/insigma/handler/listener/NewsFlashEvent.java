package com.insigma.handler.listener;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName NewsFlashEvent
 * @Description
 * @Author carrots
 * @Date 2022/7/1 16:54
 * @Version 1.0
 */
@Data
public class NewsFlashEvent extends ApplicationEvent {
    public NewsFlashEvent(Object source) {
        super(source);
    }
}
