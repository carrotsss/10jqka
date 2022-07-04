package com.insigma.handler.quote;

import com.insigma.handler.quote.command.CommandWrapper;
import com.insigma.handler.quote.command.QuoteCommandFactory;
import org.springframework.context.ApplicationEventPublisher;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SnapshotHandler
 * @Description
 * @Author carrots
 * @Date 2022/6/27 10:01
 * @Version 1.0
 */
public class SnapshotHandler implements QuoteHandler {

    @Resource
    private CacheManager cacheManager;

    @Resource
    private QuoteCommandFactory quoteCommandFactory;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private QuoteCacheKeyManager quoteCacheKeyManager;

    @Override
    public void handle(List<QuoteMsgBO> quoteMsgBOList) {
        List<CommandWrapper> commandWrappers = quoteMsgBOList.stream().flatMap(quoteMsgBO -> {
            String keyForOpenAndPre = quoteCacheKeyManager.getQuoteRediskeyForIntraDay(quoteMsgBO);
            String key = quoteCacheKeyManager.getQuoteRedisKey(quoteMsgBO);
            return quoteMsgBO.getData().entrySet().stream().map(e -> {
                if (e.getKey().equals(QuoteFiledConst.OPN_PRICE) || e.getKey().equals(QuoteFiledConst.PRE_PRICE)) {
                    return quoteCommandFactory.getHsetCommand(keyForOpenAndPre, e.getKey(), e.getValue());
                }
                return quoteCommandFactory.getHsetCommand(key, OrderbookHandler.getKey(), e.getValue());
            });
        }).collect(Collectors.toList());
        cacheManager.handle(commandWrappers);
        quoteMsgBOList.stream().forEach(o -> {
            applicationEventPublisher.publishEvent(o);
        });
    }
}
