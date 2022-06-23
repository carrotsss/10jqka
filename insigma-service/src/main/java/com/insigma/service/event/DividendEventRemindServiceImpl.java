package main.java.com.insigma.service.event;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import main.java.com.insigma.service.EventRemindService;

import java.util.List;

/**
 * @ClassName DividendEventRemindServiceImpl
 * @Description
 * @Author carrots
 * @Date 2022/6/22 15:43
 * @Version 1.0
 */
public class DividendEventRemindServiceImpl extends BasicEventRemindDataSupport<DividendDto>, ServiceImpl<> implements EventRemindService {
    private static final String BUSINESS_NAME = "DividendStockEvent";
    private static final String DIVIDEND_MONEY = "现金分红";
    private final DividendMapper dividendMapper;
    private final ExchangeRateMapper exchangeRateMapper;

    public DividendEventRemindServiceImpl(DividendMapper dividendMapper, ExchangeRateMapper exchangeRateMapper) {
        this.exchangeRateMapper = exchangeRateMapper;
        this.dividendMapper = dividendMapper;
    }

    @Override
    public boolean insert(List<DividendDto> data) {
        if (DIVIDEND_MONEY.equals(data.get(0))) {
            return false;
        }
        return update(data);
    }
}
