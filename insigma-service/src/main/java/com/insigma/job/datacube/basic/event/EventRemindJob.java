package com.insigma.job.datacube.basic.event;

import com.alibaba.fastjson.JSON;
import com.insigma.constant.CommonConstant;
import com.insigma.exception.SysRunException;
import jdk.internal.org.objectweb.asm.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName EventRemindJob
 * @Description
 * @Author carrots
 * @Date 2022/6/21 13:34
 * @Version 1.0
 */
@Component
@Slf4j
public class EventRemindJob {
    protected EventRemindService eventRemindService;

    /**
     * 处理消息
     * @param businessName
     * @param record
     * @param acknowledgment
     */
    public void handleMessage(String businessName, ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        String recordString = String.format("topic:%s,offset:%s,value:%s",
                record.topic(),
                record.partition(),
                record.offset(),
                record.value()
        );
        log.info(businessName, "大事提醒数据更新开始处理，消息：" + recordString);
        try {
            boolean processResult = process(record);
            String processResultString = processResult ? "成功" : "无更新";
            log.info("大事处理数据更新处理结束，处理结果：" + processResultString);
        } catch (Exception e) {
            log.error(businessName, "大事处理更新报错，", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    public boolean process(ConsumerRecord<String, String> record) {
        String bizname = this.getClass()
                .getSimpleName();
        if (ObjectUtils.isEmpty(eventRemindService)) {
            throw new SysRunException(CommonConstant.Common_Exception, new RuntimeException());
        }
        //解析消息
        CanalMessage<JSONObject> canalMessage = JSON.parseObject(record.value(), new TypeReference<>(){});
        if (canalMessage.isDdl()) {
            log.info(bizname, "DDL变更，勿需变更");
            return false;
        }
        //处理结果初始化
        boolean result = false;
        //服务层处理数据
        if (CanalConstants.MessageType.INSERT.name().equals(canalMessage.getType())) {
            result = eventRemindService.insert(canalMessage.getData());
        } else if (CanalConstants.MessageType.UPDATE.name(canalMessage.getType())) {
            result = eventRemindService.update(canalMessage.getData());
        } else {
            log.warn(bizname, "不处理该消息，消息类型：" + canalMessage.getType());
        }
        return result;
    }
}
