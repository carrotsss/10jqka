package com.insigma.handler.sod.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ClassName CommonArchiveHandler
 * @Description
 * @Author carrots
 * @Date 2022/6/29 10:09
 * @Version 1.0
 */
@Component
public class CommonArchiveHandler implements ArchiveHandle {
    @Autowired
    private ArchiveService arichiveService;

    public Map handle(ClearingResultMessge clearingResultMessge, ClearConfig clearConfig) {

    }

}
