package com.insigma.job.quote;

import com.fasterxml.jackson.datatype.jsr310.deser.key.PeriodKeyDeserializer;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.sql.SQLOutput;
import java.util.List;

/**
 * @ClassName QuoteClearJob
 * @Description
 * @Author carrots
 * @Date 2022/6/27 15:07
 * @Version 1.0
 */
@Component
@Slf4j
public class QuoteClearJob {
    @Resource
    private OvseSecRepository ovseSecRepository;
    @Resource
    private TradingDayApi tradingDayApi;
    @Resource(name = "quoteStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private QuoteCacheKeyManager cacheKeyManager;
    @Resource
    private QuoteMarketMapping quoteMarketMapping;

    @Resource
    private TradeTimeManager tradeTimeManager;

    @OvseJobHandler("quote-oldkey-clean")
    public void clearOldKey(OvseParam ovseJobParam) {
        Integer limit = 1000;
        String limitsql = "limit " + limit;
        String lastId = "ET0000000000";
        String params = ovseJobParam.getBizParam();
        Gson gson = new Gson();
        CleanJobParam cleanJobParam = gson.fromJson(params, CleanJobParam.class);
        if (!cleanJobParam.getCleanSwitch()) {
            return;
        }
        if (StringUtils.isEmpty(cleanJobParam.getStartId())) {
            lastId = cleanJobParam.getStartId();
        }
        String cleanDate = cleanJobParam.getCleanDate();
        if (StringUtils.isEmpty(cleanDate)) {
            cleanDate = tradeTimeManager.getTradeDateBeforeLatestDay(4);
        }
        if (StringUtils.isEmpty(cleanDate)) {
            return;
        }
        log.info("begin del key by trade date : {}", cleanDate);
        for (; ; ) {
            List<OvseSecCodeDO> codeDOSList = ovseSecRepository.lambdaQuery()
                    .gt(OvseSecCodeDO::getSecId, lastId)
                    .ge(OvseSecCodeDO::getState, 0).last(limitsql).list();
            if (CollectionUtils.isEmpty(codeDOSList)) {
                break;
            }
            String finalCleanDate = cleanDate;
            codeDOSList.forEach(o -> {
                this.hdelKey(o, finalCleanDate);
            });
            lastId = codeDOSList.get(codeDOSList.size() - 1).getSecId();
        }
    }

    public void hdelKey(OvseSecCodeDO secCodeDO, String tradeDate) {
        String quoteMarket = quoteMarketMapping.getMarketInQUote(secCodeDO);
        String intraDay = cacheKeyManager.getQuoteRedisKey(quoteMarket, secCodeDO.getSymbol(), tradeDate, null);
        String preKey = cacheKeyManager.getQuoteRedisKey(quoteMarket, secCodeDO.getSymbol(), tradeDate, TradePeriodEnum.POST_MARKET);

        System.out.println(intraDay);

        stringRedisTemplate.opsForHash().delete(intraDay);
        stringRedisTemplate.opsForHash().delete(preKey);
    }

    @Data
    public static class CleanJobParam {
        private boolean cleanSwitch;
        private String cleanDate;
        private String startId;
    }


}
