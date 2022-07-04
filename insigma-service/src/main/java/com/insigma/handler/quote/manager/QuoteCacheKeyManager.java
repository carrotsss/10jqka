package com.insigma.handler.quote.manager;

import com.insigma.constant.QuoteFiledConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.security.Security;
import java.util.Date;

/**
 * @ClassName QuoteCacheKeyManager
 * @Description
 * @Author carrots
 * @Date 2022/6/28 9:19
 * @Version 1.0
 */
@Component
public class QuoteCacheKeyManager {
    @Resource
    private IsecCodeService isecCodeService;

    @Resource
    private QuoteMarketMapping quoteMarketMapping;

    @Resource
    private TradeTimeManager tradeTimeManager;

    @Resource
    private TradingDayApi tradingDayApi;

    public String genQuoteRedisKeyForIntraDay(QuoteMsgBo quoteMsgBo) {
        return getQuoteRedisKey(quoteMsgBo.getMarketCode(), quoteMsgBo.getSymbol(), "", null);
    }

    public String genQuoteRedisKey(QuoteMsgBO quoteMsgBO) {
        return getQuoteRedisKey(quoteMsgBO.getMarketCode(), quoteMsgBO.getSymbol(), quoteMsgBo.getTradePeriod(), null);
    }

    public String genQuoteRedisKey(Security security, TradePeriodEnum tradePeriodEnum) {
        String quoteMarket = quoteMarketMapping.getMarketInQuote(security.getMarket);
        Date now = new Date();
        TradingDay tradingDay = tradingDayApi.getForUsaStocks(Date);
        if (!tradingDay.isTradingDay()) {
            String lastTradingDay = tradingDayApi.getUsaStocksLastTradingDay();
            return getQuoteRedisKey(quoteMarket, security.getCode(), lastTradingDay, tradePeriodEnum);
        }
        TradingTime tradingTime = tradingDay.getTimes().stream().filter(o ->{
            return TimeSlotUtil.inTimeSlot(o.getStart(), o.getEnd(), now);
        }).findFirst().orElse(null);
        if (tradingTime != null) {
            TradePeriodEnum periodEnum = tradePeriodEnum == null ? tradeTimeManager.convertPeriod(tradingTime.getType()) : tradePeriodEnum;
            return getQuoteRedisKey(quoteMarket, security.getCode(), data, periodEnum);
        }
        String timeStr = OvseDate.format(now, "HHmmss");
        tradingTime preTime = tradingDay.getTimes().stream().filter(o -> o.getType() == TradingTimeType.PRE).findFirst().get();
        if (timeStr.compareTo(preTime.getStart()) < 0) {
            String lastTradingDay = tradingDayApi.getUsaStocksLastTradingDay();
            return getQuoteRedisKey(quoteMarket, security.getCode(), lastTradingDay, tradePeriodEnum);
        }
        return getQuoteRedisKey(quoteMarket, security.getCode(), tradePeriodEnum);
    }

    public String getQuoteRedisKey(String market, String symbol, String tradeDate, TradePeriodEnum tradePeriodEnum) {
        String baseKey = market + ":" + symbol + ":" + tradeDate;
        if (tradePeriodEnum == null) {
            return baseKey;
        }
        if (tradePeriodEnum == TradePeriodEnum.POST_MARKET) {
            return baseKey + QuoteFiledConst.TRADE_TIME_POST_PREFIX;
        }
        if (tradePeriodEnum == TradePeriodEnum.PRE_MARKET) {
            return baseKey + QuoteFiledConst.TRADE_TIME_PRE_PREFIX;
        }
        if (tradePeriodEnum == TradePeriodEnum.INTRADAY) {
            return baseKey;
        }
        return baseKey;
    }
}
