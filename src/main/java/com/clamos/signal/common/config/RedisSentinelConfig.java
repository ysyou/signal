package com.clamos.signal.common.config;

import com.clamos.signal.common.service.RedisMessageSubscriber;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@EnableAutoConfiguration
@Configuration
@AllArgsConstructor
public class RedisSentinelConfig {

    final RedisMessageSubscriber redisMessageSubscriber;

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());

        container.addMessageListener(new MessageListenerAdapter(redisMessageSubscriber), new ChannelTopic(RedisKeys.WEBS_START_CAM));
        container.addMessageListener(new MessageListenerAdapter(redisMessageSubscriber), new ChannelTopic("test2"));
        return container;
    }

    // lettuce 사용시
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration()
                .master("mymaster")
                .sentinel("192.168.0.140", 26379)
                .sentinel("192.168.0.141", 26379)
                .sentinel("192.168.0.142", 26379);
        redisSentinelConfiguration.setPassword("Ksncio!");
        redisSentinelConfiguration.setSentinelPassword("Ksncio!");
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisSentinelConfiguration);
        return lettuceConnectionFactory;
    }

    // jedis 사용시
    /*@Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration()
                .master("mymaster")
                .sentinel("localhost", 26379)
                .sentinel("localhost", 26380)
                .sentinel("localhost", 26381);
        redisSentinelConfiguration.setPassword("str0ng_passw0rd");
        redisSentinelConfiguration.setSentinelPassword("str0ng_passw0rd");
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisSentinelConfiguration);
        return jedisConnectionFactory;
    }*/

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
