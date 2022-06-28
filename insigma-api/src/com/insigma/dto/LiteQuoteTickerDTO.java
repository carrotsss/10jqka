package com.insigma.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @ClassName LiteQuoteTickerDTO
 * @Description
 * @Author carrots
 * @Date 2022/6/27 14:12
 * @Version 1.0
 */
@Data
public class LiteQuoteTickerDTO extends LiteQuoteBaseDTO {
    private BigDecimal latestPrice;
    private BigDecimal volume;
}
