package com.insigma.bean.quote;

import com.insigma.enums.NotifyTypeEnum;
import kotlin.jvm.internal.MagicApiIntrinsics;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @ClassName QuoteMsgBO
 * @Description
 * @Author carrots
 * @Date 2022/6/28 10:12
 * @Version 1.0
 */
@Data
public class QuoteMsgBO {
    private String symbol;
    private String marketCode;
    private String exchange;
    private Integer tradeDate;
    private TradePeriodEnum tradePeriodEnum;
    private long time;
    private NotifyTypeEnum notifyTypeEnum;
    private Map<String, Object> data;

    public Object getFiled(String feild) {
        return data.get(feild);
    }
}
