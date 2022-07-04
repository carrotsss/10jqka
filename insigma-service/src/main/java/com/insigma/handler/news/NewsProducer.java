package com.insigma.handler.news;

import com.alibaba.fastjson.JSON;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

/**
 * @ClassName NewsProducer
 * @Description
 * @Author carrots
 * @Date 2022/7/1 14:48
 * @Version 1.0
 */
@Component
public class NewsProducer {

    @Value("")
    private String newPushTopic;

    @Autowired
    @Qualifier("addPagePushCounter")
    private Counter addPagePushCounter;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendNewsPush(PushRecordDto pushRecordDto) {
        sendToKafka(pushRecordDto, newPushTopic);
        //统计推送次数
        addPagePushCounter.labels(pushRecordDto.getPushOperation(), pushRecordDto.getHostifierIp()).inc();
    }

    public void sendNewsRegular(PushrecordDto pushrecordDto) {
        SendToKafka(pushRecordDto, newsRegularTopic);
    }

    public void sendToKafka(PushRecordDto pushRecordDto, String topic) {
        String messageId = pushRecordDto.getMessageId();
        String pageloadInfoJson = JSON.toJSONString(pushRecordDto);
        kafkaTemplate.send(topic, pageloadInfoJson);
        Log.info();
    }
}
