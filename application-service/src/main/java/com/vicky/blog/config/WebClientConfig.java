package com.vicky.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.vicky.blog.client.StaticServiceClient;

@Configuration
public class WebClientConfig {
    
    @Autowired
    private LoadBalancedExchangeFilterFunction loadBalancedExchangeFilterFunction;

    @Bean
    WebClient staticServiceWebClient() {
        return WebClient.builder()
                        .baseUrl("http://static-service")
                        .filter(loadBalancedExchangeFilterFunction)
                        .build();
    }

    @Bean
    StaticServiceClient staticServiceClient() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                                            .builder(WebClientAdapter.forClient(staticServiceWebClient()))
                                                            .build();
        return factory.createClient(StaticServiceClient.class);                
    }
}
