package main.java.com.insigma.job.basic.event;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * @ClassName EarningCallEventRemindJob
 * @Description
 * @Author carrots
 * @Date 2022/6/22 13:17
 * @Version 1.0
 */
@Component
public class EarningCallEventRemindJob extends EventRemindJob {
    public EarningCallEventRemindJob(EarningCallEventRemindService earningCallEventRemindService) {
        this.eventRemindService = earningCallEventRemindService;
    }

    @KafkaListener(id = "EarningCallEventRemindJob", groupId = "${event.remind.group}",
            topics = {"${event.remind.topic.earning.call}"}, containerFactory = "dataKafkaListenerContainerFactory")
    public void consumer(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        handleMessage("EarningCallJob", record, acknowledgment);
    }
}
