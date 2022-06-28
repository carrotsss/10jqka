package com.insigma.config.kafka.quote;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName QuoteKafkaConfiguration
 * @Description
 * @Author carrots
 * @Date 2022/6/28 13:41
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "quote.spring.kafka")
public class QuoteKafkaConfiguration {
    private int concurrency;
    private int pullTimeout;
}
