package com.insigma.job.datacube.basic.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

/**
 * @ClassName DividendEventRemindJob
 * @Description
 * @Author carrots
 * @Date 2022/6/21 13:36
 * @Version 1.0
 */
public class DividendEventRemindJob extends EventRemindJob {
    public DividendEventRemindJob(DividendEventRemindService dividendEventRemindService) {
        this.eventRemindService = dividendEventRemindService;
    }

    @KafkaListener(id = "DividendEventRemindJob", groupId = "${event-remidn.group}",
            topics = {"${event-remind.topic.dividend}"}, containerFactory = "dataKafkaListenerContainerFactory")
    public void consumer(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        handleMessage("DividendEventRemindJob", record, acknowledgment);
    }
}
