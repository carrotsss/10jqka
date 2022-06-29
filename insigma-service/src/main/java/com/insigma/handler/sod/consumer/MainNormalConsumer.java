package com.insigma.handler.sod.consumer;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.PolySameLen;

/**
 * @ClassName MainNormalConsumer
 * @Description
 * @Author carrots
 * @Date 2022/6/29 9:50
 * @Version 1.0
 */
@Slf4j
public class MainNormalConsumer {
    public void consumer(ClearingResultMessage clearingResultMessage) {
        try {
            docheckstatus();
            dopre();
            dealmessage();
        } catch (Exception e) {
            doError();
        }
    }

    public void dealMessage(message, topic) {

    }

    public void dealmessage(message) {

    }
}
