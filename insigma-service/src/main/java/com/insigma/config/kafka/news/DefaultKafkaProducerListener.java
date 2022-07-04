package com.insigma.config.kafka.news;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

/**
 * @ClassName DefaultKafkaProducerListener
 * @Description
 * @Author carrots
 * @Date 2022/7/1 10:58
 * @Version 1.0
 */
@Slf4j
public class DefaultKafkaProducerListener implements ProducerListener<String, String> {
    public static final String BUSINESS_NAME = "kafkaListener";

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info(BUSINESS_NAME, "success");
    }

    @Override
    public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {

    }
}


