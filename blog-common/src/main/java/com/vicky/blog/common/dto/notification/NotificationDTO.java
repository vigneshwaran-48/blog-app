package com.vicky.blog.common.dto.notification;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificationDTO {

    public enum NotificationSenderType {
        USER,
        ORGANIZATION
    }
    
    private Long id;
    private String userId;
    private String message;
    private String senderId;
    private String senderName;
    private String senderImage;
    private LocalDateTime time;
    private boolean isSeen;
    private NotificationSenderType senderType;
    private String organizationId;
    
}
