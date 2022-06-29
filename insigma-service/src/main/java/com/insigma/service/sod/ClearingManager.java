package com.insigma.service.sod;

import org.w3c.dom.css.Counter;

/**
 * @ClassName ClearingManager
 * @Description
 * @Author carrots
 * @Date 2022/6/28 17:06
 * @Version 1.0
 */
public interface ClearingManager {
    Counter PUSH_COUNTER = Counter.build()
            .name("clearing_push_counter")
            .help("clearing push message statistic")
            .labalNames("type")
            .create();

}
