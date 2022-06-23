package main.java.com.insigma.config.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

/**
 * @ClassName DataKafkaConfig
 * @Description TODO
 * @Author carrots
 * @Date 2022/6/16 11:20
 * @Version 1.0
 */
@Configuration
@EnableKafka
public class DataKafkaConfig {
    @Value("${kafka.bootstrap.yml.server}")
    private String bootstrapServers;

    @Bean("dataKafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean("dataKafkaListenerContainerFactory")
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.getContainerProperties().setPollTimeout(KafkaCommonConfig.POLL_TIMEOUT);
        return factory;
    }

    private ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(KafkaCommonConfig.consumerProps(bootstrapServers));
    }

    private ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(KafkaCommonConfig.producerProps(bootstrapServers));
    }
}
