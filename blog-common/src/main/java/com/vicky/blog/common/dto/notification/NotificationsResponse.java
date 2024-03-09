package com.vicky.blog.common.dto.notification;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class NotificationsResponse {
    
    private int status;
    private String message;
    private String path;
    private LocalDateTime time;
    private List<NotificationDTO> notifications;

}
