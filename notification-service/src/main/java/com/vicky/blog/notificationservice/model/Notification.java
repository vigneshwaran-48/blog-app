package com.vicky.blog.notificationservice.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO.NotificationSenderType;

import lombok.Data;

@Data
@Document
public class Notification {
    
    @Id
    private String id;

    private String message;
    
    private String userId;

    private LocalDateTime time;
    
    private String senderId;

    private String senderName;

    private String senderImage;

    private NotificationSenderType senderType;

    private boolean isSeen;

    public static Notification build(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setId(notificationDTO.getId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setSenderImage(notificationDTO.getSenderImage());
        notification.setSenderId(notificationDTO.getSenderId());
        notification.setTime(notificationDTO.getTime());
        notification.setUserId(notificationDTO.getUserId());
        notification.setSenderType(notificationDTO.getSenderType());
        notification.setSeen(notificationDTO.isSeen());
        notification.setSenderName(notificationDTO.getSenderName());
        return notification;
    } 

    public NotificationDTO toDTO() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(id);
        notificationDTO.setMessage(message);
        notificationDTO.setSenderImage(senderImage);
        notificationDTO.setSenderId(senderId);
        notificationDTO.setTime(time);
        notificationDTO.setUserId(userId);
        notificationDTO.setSenderType(senderType);
        notificationDTO.setSeen(isSeen);
        notificationDTO.setSenderName(senderName);

        if(senderType == NotificationSenderType.ORGANIZATION) {
            notificationDTO.setOrganizationId(senderId);
        }
        return notificationDTO;
    }
}
