package com.insigma.handler.message;

import com.insigma.constant.QuoteFiledConst;
import com.insigma.dto.LiteQuoteBaseDTO;
import com.insigma.dto.LiteQuotePriceDTO;
import com.insigma.dto.LiteQuoteTickerDTO;
import com.insigma.dto.SecurityDTO;
import com.insigma.enums.NotifyTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.Security;

/**
 * @ClassName QuoteOutputCenter
 * @Description
 * @Author carrots
 * @Date 2022/6/27 13:38
 * @Version 1.0
 */
@Component
@Slf4j
public class QuoteOutputCenter {
    @Resource
    private KafkaTemplate kafkaTemplate;

    @Resource
    private IsecCOdeService isecCOdeService;

    @Value("${task.send.switch:false}")
    private Boolean sendSwitch;

    private Gson gson = new Gson();

    @EventListener
    public void send(QuoteMsgBO quoteMsgBO) {
        if (quoteMsgBO.getNotifyTypeEnum() == NotifyTypeEnum.SNAPSHOT) {
            return;
        }
        this.send(KafkaConsts.TRADE_QUOTE_BUYSELLIST_CHANGE, quoteMsgBO.getSymbol(), gson.toJson(buildPriceChange(quoteMsgBO)));
    }

    public void send(String topic, String forKey, String message) {
        log.info("send topic :{}, message:{}", topic, message);
        if (!sendSwitch) {
            return;
        }
        kafkaTemplate.send(topic, forKey, message);
    }

    public LiteQuoteTickerDTO buildTicker(QuoteMsgBO quoteMsgBO) {
        LiteQuoteTickerDTO liteQuoteTickerDTO = new LiteQuotaTickerDTO();
        buildBase(liteQuoteTickerDTO, quoteMsgBO);
        liteQuoteTickerDTO.setLatestPrice((BigDecimal) quoteMsgBO.getData().get(QuoteFiledConst.LATEST_PRICE));
        liteQuoteTickerDTO.setVolume((BigDecimal) quoteMsgBO.getData().get(QuoteFiledConst.VOLUME));
        return liteQuoteTickerDTO;
    }

    public LiteQuotePriceDTO buildPriceChange(QuoteMsgBO quoteMsgBO) {
        LiteQuotePriceDTO liteQuotePriceDTO = new LiteQuotePriceDTO();
        buildBase(liteQuotePriceDTO, quoteMsgBO);
        return liteQuotePriceDTO;
    }

    public void buildBase(LiteQuoteBaseDTO liteQuoteBaseDTO, QuoteMsgBO quoteMsgBO) {
        liteQuoteBaseDTO.setSystemTime(System.currentTimeMillis());
        liteQuoteBaseDTO.setTradePeriod(quoteMsgBO.getTradePeriodEnum().getCode());
        liteQuoteBaseDTO.setTradeDate(quoteMsgBO.getTradeDate());
        liteQuoteBaseDTO.setEventTime(quoteMsgBO.getTime());
        SecurityDTO securityDTO = new SecurityDTO();
        securityDTO.setSymbol(quoteMsgBO.getSymbol());
        Security security = getCommonSecurity(quoteMsgBO.getSymbol());
        securityDTO.setMarket(security.getMarket());
        liteQuoteBaseDTO.setSecurityDTO(securityDTO);
    }

    public Security getCommonSecurity(String symbol) {
        Security security = isecCOdeService.getSecurityBySymbolAndState(symbol, secCodeState.COMMON.getCode());
        if (security == null) {
            return new Security("", symbol, security.getIfindId());
        }
        return security;
    }

}
