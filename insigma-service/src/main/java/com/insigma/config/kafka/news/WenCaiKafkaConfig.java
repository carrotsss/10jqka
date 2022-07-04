package com.insigma.config.kafka.news;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WenCaiKafkaConfig
 * @Description
 * @Author carrots
 * @Date 2022/7/1 13:28
 * @Version 1.0
 */
@Configuration
public class WenCaiKafkaConfig {
    @Value("${spring.wencai.kafka.bootstrap-servers}")
    private String bootStrapServer;
    private String ack;

    @Bean("wenCaiKafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producFactory());
    }

    @Bean("wenCaiKafkaListnerContainerFactory")
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    private Map<String, Object> producerProps() {
        Map<String, Object> res = new HashMap<>();
        res.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        res.put(ProducerConfig.ACKS_CONFIG, ack);
        res.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        res.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return res;
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> res = new HashMap<>();
        res.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        res.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        res.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return res;
    }
}
