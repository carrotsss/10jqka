package com.insigma.config.kafka.quote;

import lombok.Data;
import org.apache.commons.lang.BooleanUtils;

/**
 * @ClassName CustomKafkaProperties
 * @Description
 * @Author carrots
 * @Date 2022/6/28 10:53
 * @Version 1.0
 */
@Data
public class CustomKafkaProperties {
    private String brokers = "123.2.2.1:9039";
    private Boolean useSasl;
    private String jaasConf;
    private String saslMachanism;
    private String securityProtocol;
    private ConsumerProperties consumerProperties;

    @Data
    public static class ConsumerProperties{
        private Integer autoConfigInterval;
    }
}
