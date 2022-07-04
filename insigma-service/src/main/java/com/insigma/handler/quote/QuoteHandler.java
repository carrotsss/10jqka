package com.insigma.handler.quote;

/**
 * @ClassName QuoteHandler
 * @Description
 * @Author carrots
 * @Date 2022/6/27 10:00
 * @Version 1.0
 */
public interface QuoteHandler {
    void handle(List<QuoteMsgBO> quoteMsgBOList);
}
