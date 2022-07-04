package com.insigma.handler.news;

import com.insigma.constant.RedisKeyConstant;
import lombok.extern.java.Log;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.redisson.codec.MsgPackJacksonCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerExecutionChain;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @ClassName NewsConsumer
 * @Description
 * @Author carrots
 * @Date 2022/7/1 13:43
 * @Version 1.0
 */
@Component
public class NewsConsumer {
    public static final String OUT_DATA_NEWS_TOPIC = "OUT_DATA_NEWS";
    public static final String NEWS_CACHE_LOAD_TOPIC = "NEWS_CACHE_LOAD";

    @Value("#{'${news.sources}'.split(',')}")
    private List<String> sources;

    private HandlerChain<NewsSourcePo, Boolean> handlerChain;

    @PostConstruct
    private void init() {
        handlerChain = new DefaultHandleChain<>();
        handlerChain.addHandler(new RepeatCheckHandler(newSourceDataMapper));
        handlerChain.addHandler(new NewsChHandler());
        handlerChain.addHandler(new NewsHandler());
    }

    @KafkaListener(topics = {"${baseface.regular.topic}"})
    private void receiveBaseFaceRegular(ConsumerRecord<?, String> consumerRecord, Acknowledgment acknowledgment) {
        dealReceiveMessage(consumerRecord, acknowledgment, PersistRecordFlag.NONE, "basicFaceRegular");
    }

    private void dealReceiveMessage(ConsumerRecord<?, String> consumerRecord, Acknowledgment acknowledgment, PersistRecordFlag persistRecordFlag, String queueSource) {
        try {
            Optional<String> kafkaMessage = Optional.ofNullable(consumerRecord.value());
            if (kafkaMessage.isPresent()) {
                Log.info("");
                String msg = kafkaMessage.get();
                pageLoadService.dealLoadMessage(msg, persistRecordFlag, queueSource);
            }
        } catch (Exception e) {

        }finally {
            acknowledgment.acknowledge();
        }
    }

    private void savedNewsCache(String redisKey, List<NewsSourceTagPo> tagPos) {
        if (!ObjectUtils.isEmpty(tagPos)) {
            List<String> uids = tagPos.stream().map(newsSourceTagPo::getUid).collect(Collectors.toList());
            List<NewsSourcePo> newsSourcePos = newSourceDataMapper.selectByUids(uids);
            List<String> data = newsSourcePos.stream().map(OvseJson::toJson).collect(Collectors.toList());
            RedisUtils.rightPushCollect(redisKey, data, NewsConstant.NEWS_CACHE_EXPIRE, TimeUnit.DAYS);
        }
    }
    public void refreshCache(NewsSourcePo po) {
        //处理个股咨询
        if (StringUtils.hasLength(po.getUsCodes())) {
            String[] usCodes = po.getUsCodes().split(",");
            Arrays.stream(usCodes).distinct().forEach(code -> {
                String redisSizekey = String.format(RedisKeyConstant.STOCK_NEWS_SIZE, code);
                long size = newsSourceTagDataMapper.selectCOuntByStockCode(code);
                RedisUtils.set(redisSizekey, size, NewsConstant.NEWS_CACHE_EXPIRE, TimeUnit.DAYS);
                String redisDateKey = String.format(RedisKeyConstants.STOCK_NEWS_KEY, code);
                List<NewsSourceTagPo> tagPos = newsSourceTapDataMapper.selectPageDataByStockCode(code, 0, NewsConstant.NEWS_CACHE_MAX);
                RedisUtils.delete(redisDateKey);
                savedNewsCache(redisDateKey, tagPos);
            });
        }
    }
}
