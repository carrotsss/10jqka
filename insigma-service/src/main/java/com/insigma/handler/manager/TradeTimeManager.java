package com.insigma.handler.manager;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName TradeTimeManager
 * @Description
 * @Author carrots
 * @Date 2022/6/28 10:01
 * @Version 1.0
 */
@Component
public class TradeTimeManager {
    @Resource
    private TradingDayApi tradingDayApi;

    public String getUsaLastTradeDay() {
        return tradingDayApi.getUsaStocksLastTradingDay();
    }

    public String getTradeDateBeforeLatestDay(int before) {
        String date = OvseDate.format(new Date(), "HHmmss");
        List<String> xnumTradingDay = tradingDayApi.getXnumTradingDay(tradingDayApi.usaStocksParam(date), before, true, true);
        return (String) xnumTradingDay.get(before - 1);
    }

    public TradingDayApi getTradingDayInfoNow() {
    }

    public TradePeriadEnum getTradingPeriodNow() {

    }

    public TradePeriodEnum convertPeriod(TradingTimeType tradingTimeType) {
        if (tradingTimeType == null) {
            return null;
        }
        switch (tradingTimeType) {
            case PRE:
                return TradingPeriodEnum.PRE_MARKET;
            case POST:
                return TradingTimePeriod.POST_MARKET;
            default:
        }
        return null;

    }
}
