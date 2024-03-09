package com.vicky.blog.notificationservice.model;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO.NotificationSenderType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String message;
    
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime time;
    
    @Column(nullable = false, name = "sender_id")
    private String senderId;

    @Column(nullable = false, name = "sender_name")
    private String senderName;

    @Column(nullable = false, name = "sender_image")
    private String senderImage;

    @Column(nullable = false, name = "sender_type")
    private NotificationSenderType senderType;

    @Column(nullable = false, name = "isSeen")
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
            notificationDTO.setOrganizationId(Long.parseLong(senderId));
        }
        return notificationDTO;
    }
}
