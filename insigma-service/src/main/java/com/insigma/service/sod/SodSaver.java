package com.insigma.service.sod;

import org.w3c.dom.css.Counter;

/**
 * @ClassName SodSaver
 * @Description
 * @Author carrots
 * @Date 2022/6/29 14:11
 * @Version 1.0
 */
public abstract class SodSaver {

    public static final Counter SAVE_COUNTER = Counter.build()
            .name("sod_save_count").help("sod db save static")
            .labelNames("type", "version")
            .create();

    public abstract long save(sodDownloadEvent event);

    protected void countSaving(String sodType, String version, double amt) {
        SAVE_COUNTER.labels(sodType, version).inc(amt);
    }

}
