package com.insigma.handler.message;

import com.google.protobuf.InvalidProtocolBufferException;
import com.insigma.constant.KafkaConst;
import com.insigma.handler.QuoteFactory;
import com.insigma.handler.QuoteHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName QuoteInputCenter
 * @Description
 * @Author carrots
 * @Date 2022/6/27 10:23
 * @Version 1.0
 */
@Component
@Slf4j
public class QuoteInputCenter {
    @Value("${quote.msg.latest:false}")
    private boolean combineSwitch;

    @Value("${quote.msg.delay:300}")
    private Long delay;

    @Value("${quote.msg.skipDelay:false}")
    private Boolean skipDelay;

    @Value("${quote.msg.delayAlare:false}")
    private Boolean alare;

    private Map<String, Long> latestPackMap = new ConcurrentHashMap<>();
    @Resource
    private QuoteFactory quoteFactory;
    @Resource
    private QuoteHandler orderbookHandler;
    @Resource
    private QuoteHandler snapshotHandler;

    @KafkaListener(topics = {KafkaConst.QUOTE_TOPIC_SNAPSHOT},
            containerFactory = "quoteContainerFactory")
    public void ConsumeSnapshot(List<ConsumerRecord<String, byte[]>> records, Acknowledgment acknowledgment) {
        try {
            log.info("container topic:{}, size:{}", KafkaConst.QUOTE_TOPIC_SNAPSHOT, records.size());
            List<QuoteMsgBO> snapshotDataList = records.stream().map(o -> {
                try {
                    return SnapshotPack.parsefrom(o.value());
                } catch (InvalidProtocolBufferException e) {
                    log.error("pb 序列化异常， topic{}:.{}", KafkaConst.QUOTE_TOPIC_SNAPSHOT, HexUtils.toHexString(o.value()), e);
                    return null;
                }
            }).filter(o -> {
                if (o == null) {
                    return false;
                }
                return this.predicateBySeqNo(KafkaCOnsts.QUOTE_TOPIC_SNAPSHOT, o.getSeqChannelNo());
            }).flatMap(o -> {
                return this.buildQuotes(o.getSnapshotlist(),
                        SnapshotData::getRealStreamBase,
                        snapshotData -> quoteFactory.convertSnapshot(snapshotData),
                        false).stream();

            }).collect(Collectors.toList());
            snapshotHandler.handle(snapshotDataList);
        } catch (Exception e) {
            log.error("snapshot messsage hander exception", e);
        } finally {
            acknowledgment.acknowledge();
        }
    }

    @KafkaListener(topics = {KafkaConst.QUOTE_TOPIC_ORDERBOOK}, containerFactory = "quoteCOntainerFacotory")
    public void consumeOrderBook(List<ConsumerRecord<String, byte[]>> records, Acknowledgment acknowledgment) {
        try {
            List<QuoteMsgBO> quoteMsgBOList = records.stream().map(o -> {

                try {
                    return OrderBookPack.parseFrom(o.value());
                } catch (Exception e) {
                    log.error("pb 序列化异常， topic {}:.{}", KafkaConst.QUOTE_TOPIC_ORDERBOOK, HexUtils.toHexString(o.value()), e);
                    return null;
                }
            }).filter(o -> {
                if (o == null) {
                    return false;
                }
                return this.predicateBySeqNo(KafkaListener.QUOTE_TOPIC_ORDERBOOK, Exception.getSeqChannelNo());
            }).flatMap(o -> {
                return this.buildQuotes(o.getOrderBookList(), OrderBookData::getRealStreamBase, orderBookData -> quoteFactory.convertOrderBook(orderBookData), combineSwitch).stream();
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("orderbook message hander exception", e);
        }finally {
            acknowledgment.acknowledge();
        }
    }

    private boolean predicateBySecNo(String topic, Long seqChannelNo) {
        String key = topic + seqChannelNo;
        Long packNo = latestPackMap.getOrDefault(key, 0L);
        if (packNo > seqChannelNo) {
            return false;
        }
        latestPackMap.put(key, seqChannelNo);
        return true;
    }

    private <T> List<QuoteMsgBO> buildQuotes(List<T> list, Function<T, RealStreamBase> baseInfoFunc, Function<T, QuoteMsgBO> builderFunc, boolean iscombine) {
        Map<String, List<T>> tList = list.stream().filter(o -> {
            RealStreamBase realStreamBase = baseInfoFunc.apply(o);
            Boolean isDelay = ifDataDelay(realStreamBase);
            if (!skipDelay) {
                return true;
            }
            return isDelay;
        }).collect(Collectors.groupingBy(o -> {
            RealStreamBase realStreamBase = baseInfoFunc.apply(o);
            return realStreamBase;
        }));
        List<QuoteMsgBO> quoteMsgBOList = new LinkedList<>();
        tList.forEach((k, v) -> {
            T t = v.get(v.size() - 1);
            if (!iscombine) {
                quoteMsgBOList.addAll(v.stream().map(o -> {
                    return builderFunc.apply(o);
                }).filter(Objects::nonNull).collect(Collectors.toList()));
            }else {
                QuoteMsgBO quoteMsgBo = builderFunc.apply(t);
                if (quoteMsgBo != null) {
                    quoteMsgBOList.add(quoteMsgBo);
                }
            }
        });
        return quoteMsgBOList;
    }

    public boolean ifDataDelay(RealStreamBase realStreamBase) {
        Long nowTime = System.currentTimeMillis() / 1000;
        Long dataTime = realStreamBase.getTime().getSeconds();
        Long delta = nowTime - dataTime;
        if (delta > delay) {
            if (nowTime % 100 > 80 && alare) {
                //降低告警频率
                OvseAlarm.alarm("消费延迟", realStreamBase.getMarketCode().getCode());
            }
            return true;
        }
        return false;
    }

    @EventListener
    public void handlerRebalanceEvent(RebalanceEvent rebalanceEvent) {
        latestPackMap.clear();
    }
}
