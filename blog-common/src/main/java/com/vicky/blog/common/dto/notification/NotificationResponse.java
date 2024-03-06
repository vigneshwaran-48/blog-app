package com.vicky.blog.common.dto.notification;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private NotificationDTO notification;
    
}
