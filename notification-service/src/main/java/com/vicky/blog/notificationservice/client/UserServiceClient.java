package com.vicky.blog.notificationservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import com.vicky.blog.common.dto.user.UserResponseData;

@HttpExchange("/api/v1/app")
public interface UserServiceClient {
    
    @GetExchange("/{userId}")
    public UserResponseData getUser(@PathVariable("userId") String userId);
}
