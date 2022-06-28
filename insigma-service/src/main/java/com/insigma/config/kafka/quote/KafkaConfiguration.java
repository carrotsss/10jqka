package com.insigma.config.kafka.quote;

import com.insigma.handler.message.RebalanceEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName KafkaConfiguration
 * @Description
 * @Author carrots
 * @Date 2022/6/28 10:59
 * @Version 1.0
 */
@Configuration
public class KafkaConfiguration {
    @Resource
    private QuoteKafkaProperties quoteKafkaProperties;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean("tradeKafkaProperties")
    @ConfigurationProperties(prefix = "trade.spring.kafka")
    @Primary
    public KafkaProperties tradeKafkaProperties() {
        return new KafkaProperties();
    }

    @Bean("quoteContainerFactory")
    @Primary
    public ConcurrentKafkaListenerContainerFactory kafkaListenerContainerFactory(QuoteKafkaProperties quoteKafkaProperties) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(configProperties(quoteKafkaProperties)));
        factory.setConcurrency(quoteKafkaProperties.getConcurrency());
        factory.setAutoStartup(true);
        factory.setBatchListener(true);

        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(quoteKafkaProperties.getPullTimeout());
        factory.getContainerProperties().setPollTimeout(rebalanceNotify());
        return factory;
    }

    @Bean
    public ConsumerRebalanceListener rebalanceNotify() {
        return (ConsumerRebalanceListener) (consumer, partitions) -> {
            partitions.forEach(o -> {
                applicationEventPublisher.publishEvent(new RebalanceEvent(o.topic()));
            });
        };
    }

    private void configNotEmptyProperties(Map<String, Object> configMap, String key, Object value) {
        if (value != null) {
            configMap.put(key, value);
        }
    }

    private Map<String, Object> configProperties(CustomKafkaProperties kafkaProperties) {
        Map<String, Object> configMap = new HashMap<>();
        QuoteKafkaProperties.ConsumerProperties consumerProperties = kafkaProperties.getConsumerProperties();
        if (kafkaProperties.getUseSasl()) {
            this.configNotEmptyProperties(configMap, ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBrokers());
            this.configNotEmptyProperties(configMap, SaslConfigs.SASL_JAAS_CONFIG, kafkaProperties.getJaasConf());
        }
        if (consumerProperties.getGroupId() != null) {
            this.configNotEmptyProperties(configMap, ConsumerConfig.GROUP_ID_CONFIG, consumerProperties.getGroupId());
        }
        return configMap;
    }

    @Bean("tradeKafkaTemplate")
    public KafkaTemplate<String, Object> tradeKafkaTemplate(@Qualifier("tradeKafkaProperties") KafkaProperties kafkaProperties) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getProducer().getRetries());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getProducer().getAcks());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        ProducerFactory kafkaProducerFactor = new DefaultKafkaProducerFactory(props);
        return new KafkaTemplate<>(kafkaProducerFactor, true);
    }
}
