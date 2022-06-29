package com.insigma.handler.sod.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ArchiveHandlerFactory
 * @Description
 * @Author carrots
 * @Date 2022/6/29 10:17
 * @Version 1.0
 */
@Component
public class ArchiveHandlerFactory {
    @Autowired
    private CommonArchiveHandler commonArchiveHandler;

    private Map<String, ArchiveHandle> handlers;

    @PostConstruct
    private void init() {
        handlers = new HashMap<>();
        handlers.put("common", commonArchiveHandler);
    }

    public ArchiveHandle getHandler(String type) {
        return handlers.get(type);
    }
}
