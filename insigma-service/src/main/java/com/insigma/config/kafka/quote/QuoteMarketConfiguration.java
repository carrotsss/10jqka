package com.insigma.config.kafka.quote;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName QuoteMarketConfiguration
 * @Description
 * @Author carrots
 * @Date 2022/6/28 13:44
 * @Version 1.0
 */
@Configuration
public class QuoteMarketConfiguration {
    @Bean("marketMapping")
    @ConfigurationProperties(prefix = "quote.market.map")
    public Map<String, String> marketMapping() {
        return new HashMap<>();
    }
}
