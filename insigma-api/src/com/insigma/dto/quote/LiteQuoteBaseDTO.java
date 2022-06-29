package com.insigma.dto.quote;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LiteQuoteBaseDTO
 * @Description
 * @Author carrots
 * @Date 2022/6/23 14:06
 * @Version 1.0
 */
@Data
public class LiteQuoteBaseDTO implements Serializable {
    private SecurityDTO securityDTO;
    private Integer tradeDate;
    private Integer tradePeriod;
    private Long eventTime;
    private long systemTime;
}
