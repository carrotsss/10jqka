package com.insigma.job.news;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ExchangeRateInitJob
 * @Description
 * @Author carrots
 * @Date 2022/6/29 16:43
 * @Version 1.0
 */
public class ExchangeRateInitJob implements CommandLineRunner {

    public static final Integer CORE_POOL_SIZE = 10;
    public static final Integer MAX_POOL_SIZE = 10;
    public static final int KEEP_ALIVE_TIME = 0;
    public static final Integer SLEEP_TIME = 100;

    public static final Integer EXCHANGE_RATE_QUERY_SIZE = 1000;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @Override
    public void run(String... args) throws Exception {
        List<ExchangeRateProgramDto> programData = ExchangeRateService.getProgramByProgram();
        if (CollectionUtils.isEmpty(programData)) {
            Log.warn("");
            XxlJobLogger.log();
        }
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("exchange-rate-pool-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                nameThreadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );
        //遍历配置更新缓存
        programData.forEach(value ->
                executor.execute(new ExchangeRateTask(value))
        );
        executor.shutdown();
        log.info("exchange rate job finish, executor pool is terminating {}", executor.isTerminated());
    }

    @OvseJobHandler("exchange-rate-refresh")
    public void execute(OvseJobParam param) {

        try {
            XxlJobLogger.log();
            run();
            XxlJobLogger.log();
        } catch (Exception e) {

        }
    }

    private class ExchangeRateTask implements Runnable {

        private final ExchangeRateProgramDto programDto;

        public ExchangeRateTask(ExchangeRateProgramDto programDto) {
            this.programDto = programDto;
        }

        @Override
        public void run() {
            updateExchangeRate(programDto);
        }
    }

    public void updateExchangeRate(ExchangeRateProgramDto exchangeRateProgramDto) {
        //获取全部数据
        List<ExchangeRateVO> result = getExchangeRateVaueList(exchangeRateProgramDto);
        //无数据退出
        if (CollectionUtils.isEmpty(result)) {
            String message = String.format("update exchange cache, exchange rate data is empty", exchangeRateProgramDto.toString());
            log.warn(message);
            XxlJobLogger.log(messge);
            return;
        }
        result.sort(Comparator.comparing(ExchangeRateVO::getDate));
        ExchangeRateQuery rateQuery = new ExchangeRateQuery();
        rateQuery.setProgramNumber(exchangeRateProgramDto.getProgramNumber);
        boolean updateSuccess = exchangeRateService.updateExchangeRateCache(rateQuery, result);
        if (updateSuccess) {

        }
    }

    public List<ExchangeRateVO> getExchangeRateVaueList(ExchangeRateProgramDto exchangeRateProgramDto) {
        if (ObjectUtils.isEmpty(exchangeRateProgramDto)) {
            log.warn("update exchange cache failed , exchange rate program is empty");
            return Collections.emptyList();
        }
        List<ExchangeRateVO> resutl = new ArrayList();
        int page = 1;
        ExchangePatePageQuery pageQuery = new ExchangeRatePageQurey();
        pageQuery.setProgramNumber(exchangeRateProgramDto.getProgramNumber);
        pageQuery.setSize(EXCHANGE_RATE_QUERY_SIZE);
        while (true) {
            pageQuery.setPage(page);
            List<ExchangeRateDto> currentPageData = exchangeRateService.getExchangeRateByProgramAndPage(pageQuery);
            if (CollectionUtils.isEmpty(currentPageData)) {
                break;
            }
            currentPageData.forEach(o -> resutl.add(new ExchageRateVO(o.getData(), o.getRate())));
            page++;
        }
        return resutl;
    }

}
