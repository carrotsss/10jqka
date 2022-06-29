package com.insigma.handler.sod.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

/**
 * @ClassName RemoteSodVersionLoader
 * @Description
 * @Author carrots
 * @Date 2022/6/28 15:18
 * @Version 1.0
 */
@Slf4j
@ConditionalOnMissingClass(value = "com.jqka.ovse.cms.sod.service.impl.SodVersionImpl")
@Component
public class RemoteSodVersionLoader implements SodVersionLoader{
    private SodVersionFeign sodVersionFeign;

    @Autowired
    public RemoteSodVersionLoader(SodVersionFeign sodVersionFeign) {
        log.info("init RemoteSodVersionLoader");
        this.sodVersionFeign = sodVersionFeign;
    }

    @Override
    public SodVersion load() {
        return sodVersionFeign.get();
    }
}

