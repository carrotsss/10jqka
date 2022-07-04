package com.insigma.service.quote;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.insigma.bean.quote.OvseSecCodeDO;
import com.insigma.bean.quote.Security;
import com.insigma.dao.OvseSecRepository;
import kotlin.jvm.internal.Lambda;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import javax.naming.Name;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SecCodeServiceImpl
 * @Description
 * @Author carrots
 * @Date 2022/6/28 10:35
 * @Version 1.0
 */
public class SecCodeServiceImpl implements ISecCodeService {
    @Resource
    @Lazy
    private OvseSecRepository ovseSecRepository;


    @Override
    @Cached(name = "INFINDID", key = "#symbol+':'+#market",
            localLimit = 2000, empire = 300, timeUnit = TimeUnit.SECONDS, cacheType = CocheType.BOTH, cacheNullValue = false)
    public String getIfindIdBySymbol(String symbol, String market) {
        LambdaQueryWrapper<OvseSecCodeDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OvseSecCodeDO::getMarket, market);
        queryWrapper.eq(OvseSecCodeDO::getSymbol, symbol);
        OvseSecCodeDO secCodeDO = ovseSecRepository.getOne(queryWrapper);
        if (secCodeDO != null) {
            return secCodeDO.getSecId();
        }
        return null;
    }

    @Override
    public Security getSecurityByIfindId(String ifindId) {
        return null;
    }

    @Override
    public Security getSecurityBySymbolAndMarket(String symbol, String market) {
        return null;
    }

    @Override
    public Security getSecurityBySymbolAndStart(String symbol, String state) {
        return null;
    }
}
