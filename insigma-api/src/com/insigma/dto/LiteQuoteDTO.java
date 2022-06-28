package com.insigma.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName LiteQuoteDTO
 * @Description
 * @Author carrots
 * @Date 2022/6/27 14:06
 * @Version 1.0
 */
public class LiteQuoteDTO implements Serializable {
    private SecurityDTO securityDTO;
    private BigDecimal buy1stPrice;
    private BigDecimal sell1stPrice;
    private BigDecimal openPrice;
    private BigDecimal prePrice;
    private BigDecimal latestPrice;
    private BigDecimal volume;
    private MarketInfoDTO preMarket;
    private MarketInfoDTO afterMarket;
    private Integer tradeDate;
    private Integer tradePeriod;
    private Long eventTime;
    private long systemTime;
}
