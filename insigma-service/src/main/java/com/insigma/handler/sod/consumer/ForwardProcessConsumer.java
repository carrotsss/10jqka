package com.insigma.handler.sod.consumer;

import com.google.common.util.concurrent.ListenableFuture;
import com.insigma.constant.SodConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ForwardProcessConsumer
 * @Description
 * @Author carrots
 * @Date 2022/6/28 17:24
 * @Version 1.0
 */
@Slf4j
public class ForwardProcessConsumer extends MainNormalConsumer {
    public static final String FID_CODE_HAS_DATA = "1";

    @Value("${clearing.control.step.calloms:http}")
    private String calloms;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private WebGateUtil webGateUtil;

    @Autowired
    private ClearingVersionService clearingVersionService;

    @Autowired
    private ClearingNotifyService clearingNotifyService;

    public void consumer(ClearingResultMessage message) {
        try {
            super.doCHeckStatus(message);
            super.doPre(message);
            if (CALL_OMS_HTTP.equals(calloms)) {
                this.dealMessageForHttp(message);
            } else if (CALL_OMS_KAFKA.equals(calloms)) {
                this.dealMessageForKafka(message);
            } else {
                String title = "";
                String errmsg = "";
                doError();
                clearingNotifyService.alarm(titel, errmsg);
            }
        } catch (Exception e) {
            super.doError(messgge, e.getMessage());
        }
    }

    private void dealMessageForKafka(ClearingResultMessage clearingResultMessage) {
        ClearingStep step = clearingResultMessage.getType();
        ClearingPushMessage clearingPushMessage = new ClearingPushMessage(clearingResultMessage.getProcess(), clearingResultMessage.getVersion());
        String kafkaKey = Optional.ofNullable(clearingResultMessage.getAccount()).orElse(StringUtils.EMPTY);
        String topic = SodConstant.CLEARING_PUSH_TOPIC;
        ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, kafkaKey, clearingPushMessage);
        future.get(SodConstant.KAFKA_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    private  void dealMessageForHttp() {

    }
}

