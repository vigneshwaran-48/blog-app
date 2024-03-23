package com.vicky.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.vicky.blog.client.NotificationServiceClient;
import com.vicky.blog.client.StaticServiceClient;
import com.vicky.blog.filter.HTTPClientExchangeFilter;

@Configuration
public class WebClientConfig {
    
    @Autowired
    private HTTPClientExchangeFilter httpClientExchangeFilter;

    @Value("${services.api-gateway.base}")
    private String apiGatewayBase;

    @Bean
    WebClient apiGatewayWebClient() {
        return WebClient.builder()
                        .baseUrl(apiGatewayBase)
                        .filter(httpClientExchangeFilter)
                        .build();
    }

    @Bean
    StaticServiceClient staticServiceClient() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                                            .builder(WebClientAdapter.forClient(apiGatewayWebClient()))
                                                            .build();
        return factory.createClient(StaticServiceClient.class);                
    }

    @Bean
    NotificationServiceClient notificationServiceClient() {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                                                        .builder(WebClientAdapter.forClient(apiGatewayWebClient()))
                                                        .build();
        return factory.createClient(NotificationServiceClient.class);
    }
}
