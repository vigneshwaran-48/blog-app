package com.vicky.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
}
