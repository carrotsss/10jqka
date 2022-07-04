package com.insigma.handler.quote.command;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName QuoteCommandFactory
 * @Description
 * @Author carrots
 * @Date 2022/6/23 16:13
 * @Version 1.0
 */
@Component
public class QuoteCommandFactory {
    @Resource(name = "quoteStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private QuoteCacheKeyManager quoteCacheKeyManager;

    public static final String HSET_COMMAND = "hset";

    public CommandWrapper getHsetCommand(String key, String field, Object value) {
        RedisSerializer keySerializer = stringRedisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = stringRedisTemplate.getValueSerializer();
        CommandWrapper commandWrapper;
        commandWrapper = new CommandWrapper();
        commandWrapper.setCommandType(HSET_COMMAND);
        if (value instanceof BigDecimal) {
            commandWrapper.setValue(valueSerializer.serialize(((BigDecimal) value).toPlainString()));
        } else {
            commandWrapper.setValue(valueSerializer.serialize(value));
        }
        return commandWrapper;
    }

    public List<CommandWrapper> getCommands(QuoteMsgBo quoteMsgBo) {
        if (CollectionUtils.isEmpty(quoteMsgBo.getData())) {
            return Collections.emptyList();
        }
        String key = getKey(quoteMsgBo);
        return quoteMsgBo.getData().entrySet().stream().map(e -> {
            return getHsetCommand(key, e.getKey(), e.getValue());
        }).collect(Collectors.toList());
    }

    private String getKey(QuoteMsgBo quoteMsgBo) {
        return quoteCacheKeyManager.genQuoteRedisKey(quoteMsgBo);
    }
}
