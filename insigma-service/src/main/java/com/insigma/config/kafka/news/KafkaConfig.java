package com.insigma.config.kafka.news;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.tomcat.jni.Proc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.time.format.SignStyle;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName KafkaConfig
 * @Description
 * @Author carrots
 * @Date 2022/7/1 11:01
 * @Version 1.0
 */
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("#{} ")
    private String retry;

    private String batchSize;

    private String bufferMemory;
    private String liner;
    private String groupId;
    private String autoOffset;

    private String autoCommit;
    private String enableAutoCommit;

    @Primary
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {

        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerListener(new DefaultKafkaProducerListener());
        return kafkaTemplate;
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

    private ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerProps());
    }

    private ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    private Map<String, Object> producerProps() {
        Map<String, Object> res = new HashMap<>();
        res.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        res.put(ProducerConfig.RETRIES_CONFIG, retry);
        res.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        res.put(ProducerConfig.LINGER_MS_CONFIG, liner);
        res.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        res.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        res.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return res;
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> res = new HashMap<>();
        res.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        res.put(ProducerConfig.RETRIES_CONFIG, retry);
        res.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        res.put(ProducerConfig.LINGER_MS_CONFIG, liner);
        res.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        res.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        res.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return res;
    }
}
