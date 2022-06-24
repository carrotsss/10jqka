package com.insigma.handler;

import com.insigma.handler.command.CommandWrapper;
import com.insigma.handler.command.QuoteCommandFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @ClassName OrderbookHandler
 * @Description
 * @Author carrots
 * @Date 2022/6/24 16:49
 * @Version 1.0
 */
@Component
public class OrderbookHandler extends QuoteHandler {
    @Resource
    private QuoteCommandFactory quoteCommandFactory;

    @Resource
    private CacheManager cacheManager;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    private Map<String, QuoteMsgBo> latestMsg = new ConcurrentHashMap<>();

    public void handle(List<QuoteMsgBo> quoteMsgBos) {
        List<CommandWrapper> commandWrappers = quoteMsgBos.stream().flatMap(o -> {
            return quoteCommandFactory.getCommands(o).stream();
        }).collect(Collectors.toList());
        cacheManager.handle(commandWrappers);
        quoteMsgBos.forEach(o->{
            if (!hashChange(o)) {
                return;
            }
            applicationEventPublisher.publishEvent(o);
        });
    }

    //买一卖一发成变化
    public boolean hasChange(QuoteMsgBO quoteMsgBo) {
        String key = quoteMsgBo.getSymbol() + ":" + quoteMsgBo.getMarketCode();
        QuoteMsgBO oldMsg = latestMsg.get(key);
        if (oldMsg == null) {
            latestMsg.put(key, quoteMsgBo);
            return true;
        }
        BigDecimal sell1st = (BigDecimal) quoteMsgBo.getFiled(QuoteFileCount.SELL_PRICE_1ST);
        BigDecimal buy1st = (BigDecimal) quoteMsgBo.getFiled(QuoteFileCount.BUY_PRICE_1ST);
        BigDecimal oldSell1st=
    }
}
