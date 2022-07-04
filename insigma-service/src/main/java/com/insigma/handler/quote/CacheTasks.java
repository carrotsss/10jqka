package com.insigma.handler.quote;

import com.insigma.handler.quote.command.CommandWrapper;
import com.insigma.handler.quote.command.QuoteCommandFactory;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.SlotHash;
import io.lettuce.core.dynamic.RedisCommandFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClusterConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName CacheTasks
 * @Description
 * @Author carrots
 * @Date 2022/6/24 16:07
 * @Version 1.0
 */
@Component
public class CacheTasks {
    @Resource(name = "quoteStringRedisTemplate")
    private StringRedisTemplate stringRedisTemplate;

    public List<Object> invoke(List<CommandWrapper> commandWrappers) {
        List<Object> objects = stringRedisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                commandWrappers.forEach(o -> {
                    switch (o.getCommandType()) {
                        case QuoteCommandFactory.HSET_COMMAND:
                            connection.hashCommands().hSet(o.getKey(), o.getField(), o.getValue());
                            return;
                        default:
                            throw new RuntimeException("not support commad");
                    }
                });
                return null;
            }
        });
        return objects;
    }

    public RedisCommandFactory getLettuceChannel(String key) {
        RedisConnectionFactory redisConnectionFactory = stringRedisTemplate.getConnectionFactory();
        if (!(redisConnectionFactory instanceof LettuceConnectionFactory)) {
            throw new RuntimeException("method is valid");
        }
        LettuceConnectionFactory lettuceConnectionFactory = (LettuceConnectionFactory) redisConnectionFactory;
        LettuceClusterConnection lettuceClusterConnection = (LettuceClusterConnection) lettuceConnectionFactory.getClusterConnection();
        int slot = SlotHash.getSlot(key);
        RedisClusterNode redisClusterNode = lettuceClusterConnection.clusterGetNodeForSlot(slot);
        RedisClusterClient clusterClient = (RedisClusterClient) (((LettuceConnectionFactory) redisConnectionFactory).getNativeClient());
        StatefulRedisConnection statefulRedisConnection = clusterClient.connect().getConnection(redisClusterNode.getId());
        RedisCommandFactory redisCommandFactory = new RedisCommandFactory(statefulRedisConnection);
        return redisCommandFactory;
    }
}
