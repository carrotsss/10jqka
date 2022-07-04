package com.insigma.config.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.internal.LettuceFactories;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.expression.PropertyAccessor;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName RedisCongfiguration
 * @Description
 * @Author carrots
 * @Date 2022/6/28 13:46
 * @Version 1.0
 */
@Configuration
@Slf4j
public class RedisCongfiguration {
    public static final long ONE = 1L;
    private static final long TWO = 2L;
    private static final long TEN = 10L;

    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        Set<String> cacheNames = new HashSet<>();
        cacheNames.add("2m");
        cacheNames.add("10m");
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>(cacheNames.size());
        redisCacheConfigurationMap.put("10m", configuration.entryTtl(Duration.ofMinutes(TEN)));
        redisCacheConfigurationMap.put("2m", configuration.entryTtl(Duration.ofMinutes(TWO)));
        redisCacheConfigurationMap.put("1h", configuration.entryTtl(Duration.ofHours(ONE)));
        return RedisCacheManager.builder().initialCacheNames(cacheNames)
                .withInitialCacheConfigurations(redisCacheConfigurationMap).transactionAware().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "quote.spring.redis.pool")
    public GenericObjectPoolConfig poolConfig() {
        return new GenericObjectPoolConfig();
    }

    @Bean("quoteProperties")
    @Primary
    @ConfigurationProperties(prefix = "quote.spring.redis")
    public RedisProperties quoteProperties() {
        return new RedisProperties();
    }

    @Bean("quoteReidisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory(@Qualifier("quoteProperties") RedisProperties redisProperties, GenericObjectPoolConfig genericObjectPoolConfig) {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        configuration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder().poolConfig(genericObjectPoolConfig).build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, clientConfiguration);
        return factory;
    }

    @Bean("quoteRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Qualifier("quoteReidsConnectionFactory") RedisConnectionFactory connectionFactory) {

        RedisTemplate redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean("quoteStringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(@Qualifier("quoteRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        log.info("loading Default String reids template");
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<Object> jsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jsonRedisSerializer);
        return redisTemplate;
    }
}
