package com.vicky.blog.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.vicky.blog.common.dto.redis.UserAccessDetails;

@Configuration
public class RedisConfig {

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setHostName("redis-service");
        return factory;
    }

    @Bean
    RedisTemplate<String, UserAccessDetails> redisTemplate() {
        RedisTemplate<String, UserAccessDetails> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        return template;
    }

    @Bean
    @Profile("local || docker")
    RedisConfiguration localRedisConfiguration() {
        return new RedisConfiguration(5L, TimeUnit.MINUTES);
    }

    @Bean
    @Profile("prod")
    RedisConfiguration productionRedisConfiguration() {
        return new RedisConfiguration(1L, TimeUnit.DAYS);
    }
}
