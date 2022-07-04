package com.insigma.handler.quote.manager;

import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ClassName QuoteMarketMapping
 * @Description
 * @Author carrots
 * @Date 2022/6/28 9:50
 * @Version 1.0
 */
@Component
public class QuoteMarketMapping {
    @Resource
    private Map<String, String> marketMapping;

    public String getMarketInQuote(String market) {
        if (market.equals(MarketCode.NQ.name())) {
            return QuoteMarketEnum.NQ.getMarket();
        }
        String quoteMarket = marketMapping.get(market);
        if (StringUtils.isNotEmpty(quoteMarket)) {
            return quoteMarket;
        }
        return quoteMarketEnum.NY.getMarket();
    }

}
