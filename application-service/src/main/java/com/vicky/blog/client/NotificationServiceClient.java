package com.vicky.blog.client;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationResponse;

@HttpExchange("/api/v1/notification")
public interface NotificationServiceClient {
    
    @PostExchange
    public NotificationResponse addNotification(@RequestBody NotificationDTO notificationDTO);
    
}
