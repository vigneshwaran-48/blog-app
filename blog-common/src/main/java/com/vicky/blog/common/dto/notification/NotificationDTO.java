package com.vicky.blog.common.dto.notification;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationDTO {
    
    private Long id;
    private String userId;
    private String message;
    private String notificationSenderName;
    private String notificationSenderImage;
    private LocalDateTime time;
    
}
