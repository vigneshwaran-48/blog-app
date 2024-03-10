package com.vicky.blog.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

import com.vicky.blog.util.UserContextHolder;

import reactor.core.publisher.Mono;

@Component
public class HTTPClientExchangeFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {

        String accessToken = UserContextHolder.getContext().getAccessToken();
        ClientRequest newRequest = ClientRequest.from(request)
                                                .header("Authorization", "Bearer " + accessToken)
                                                .build();
        
        return next.exchange(newRequest);
    }

}
