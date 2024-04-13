package com.vicky.blog.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.vicky.blog.common.dto.redis.UserAccessDetails;

@Configuration
public class RedisConfig {

    @Value("${app.redis.host}")
    private String redisHostName;

    @Value("${app.redis.port}")
    private String port;

    @Value("${app.redis.password}")
    private String password;

    @Bean
    @Profile("docker || local")
    LettuceConnectionFactory lettuceConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setHostName(redisHostName);
        factory.setPort(Integer.parseInt(port));
        return factory;
    }

    @Bean("lettuceConnectionFactory")
    @Profile("prod")
    LettuceConnectionFactory productionLettuceConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setHostName(redisHostName);
        factory.setPort(Integer.parseInt(port));
        factory.setPassword(password);
        factory.setUseSsl(true);
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
