package com.insigma.handler;

import com.insigma.constant.QuoteFiledConst;
import com.insigma.handler.command.CommandWrapper;
import com.insigma.handler.command.QuoteCommandFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
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
public class OrderbookHandler implements QuoteHandler {
    @Resource
    private QuoteCommandFactory quoteCommandFactory;

    @Resource
    private CacheManager cacheManager;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    private Map<String, QuoteMsgBo> latestMsg = new ConcurrentHashMap<>();

    @Override
    public void handle(List<QuoteMsgBO> quoteMsgBOList) {
        List<CommandWrapper> commandWrappers = quoteMsgBos.stream().flatMap(o -> {
            return quoteCommandFactory.getCommands(o).stream();
        }).collect(Collectors.toList());
        cacheManager.handle(commandWrappers);
        quoteMsgBos.forEach(o -> {
            if (!hasChange(o)) {
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
        BigDecimal sell1st = (BigDecimal) quoteMsgBo.getFiled(QuoteFiledConst.SELL_PRICE_1ST);
        BigDecimal buy1st = (BigDecimal) quoteMsgBo.getFiled(QuoteFiledConst.BUY_PRICE_1ST);
        BigDecimal oldSell1st = (BigDecimal) quoteMsgBo.getFiled(QuoteFiledConst.SELL_PRICE_1ST);

        latestMsg.put(key, quoteMsgBo);
        if ((oldSell1st != null && oldSell1st.compareTo(sell1st) == 0) && (oldSell1st != null && oldSell1st.compareTo(buy1st) == 0)) {
            return false;
        }
        return true;
    }

}
