package com.insigma.job.news;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import jodd.util.CommandLine;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @ClassName ExchangeRateInitJob
 * @Description
 * @Author carrots
 * @Date 2022/6/29 16:43
 * @Version 1.0
 */
public class ExchangeRateInitJob implements CommandLineRunner {


    @Override
    public void run(String... args) throws Exception {
        List<ExchangeRateProgramDto> programData = ExchangeRateService.getProgramByProgram();
        if (CollectionUtils.isEmpty(programData)) {
            Log.warn("");
            XxlJobLogger.log();
        }
        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("exchange-rate-pool-%d").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE
        )
    }
}
