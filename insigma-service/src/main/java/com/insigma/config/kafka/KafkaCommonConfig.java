package main.java.com.insigma.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName KafkaCommonConfig
 * @Description
 * @Author carrots
 * @Date 2022/6/16 13:32
 * @Version 1.0
 */
public final class KafkaCommonConfig {
    private static final int CONFIG_MAP_SIZE = 15;
    public static final int POLL_TIMEOUT = 3000;

    private KafkaCommonConfig() {

    }

    public static Map<String, Object> consumerProps(String bootstrapServers) {
        Map<String, Object> res = new HashMap<>(CONFIG_MAP_SIZE);
        res.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        res.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        res.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return res;
    }

    public static Map<String, Object> producerProps(String bootstrapServers) {
        Map<String, Object> res = new HashMap<>(CONFIG_MAP_SIZE);
        res.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        res.put(ProducerConfig.RETRIES_CONFIG, 0);
        res.put(ProducerConfig.ACKS_CONFIG, "1");
        res.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        res.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return res;
    }
}
