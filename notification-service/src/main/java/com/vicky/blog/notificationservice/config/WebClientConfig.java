package com.vicky.blog.notificationservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.vicky.blog.notificationservice.client.OrganizationServiceClient;
import com.vicky.blog.notificationservice.client.UserServiceClient;
import com.vicky.blog.notificationservice.filter.HTTPClientExchangeFilter;

@Configuration
public class WebClientConfig {
    
    @Value("${services.api-gateway.base}")
    private String apiGatewayBase;

    @Autowired
    private HTTPClientExchangeFilter httpClientExchangeFilter;

    @Bean
    WebClient webClient() {
        return WebClient.builder()
            .baseUrl(apiGatewayBase)
            .filter(httpClientExchangeFilter)
            .build();
    }
    
    @Bean
    UserServiceClient userServiceClient() {
        HttpServiceProxyFactory httpServiceProxyFactory =
            HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient()))
                .build();
        return httpServiceProxyFactory.createClient(UserServiceClient.class);
    }

    @Bean
    OrganizationServiceClient organizationServiceClient() {
        HttpServiceProxyFactory httpServiceProxyFactory =
            HttpServiceProxyFactory.builder(WebClientAdapter.forClient(webClient()))
                .build();
        return httpServiceProxyFactory.createClient(OrganizationServiceClient.class);
    }
}
