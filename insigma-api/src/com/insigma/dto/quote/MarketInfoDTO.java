package com.insigma.dto.quote;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName MarketInfoDTO
 * @Description
 * @Author carrots
 * @Date 2022/6/27 14:13
 * @Version 1.0
 */
public class MarketInfoDTO implements Serializable {
    private BigDecimal latestPrice;
    private BigDecimal volume;
}
