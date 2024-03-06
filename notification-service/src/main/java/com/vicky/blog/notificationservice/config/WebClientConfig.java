package com.vicky.blog.notificationservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.vicky.blog.notificationservice.client.UserServiceClient;

@Configuration
public class WebClientConfig {
    
    @Value("${services.api-gateway.base}")
    private String apiGatewayBase;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
            .baseUrl(apiGatewayBase)
            .build();
    }
    
    @Bean
    UserServiceClient userServiceClient() {
        HttpServiceProxyFactory httpServiceProxyFactory =
            HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient()))
                .build();
        return httpServiceProxyFactory.createClient(UserServiceClient.class);
    }
}
