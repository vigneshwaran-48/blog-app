package com.vicky.blog.notificationservice.model;

import java.time.LocalDateTime;

import com.vicky.blog.common.dto.notification.NotificationDTO;

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
    
    @Column(nullable = false, name = "sender_name")
    private String senderName;

    @Column(nullable = false, name = "sender_image")
    private String senderImage;

    public static Notification build(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setId(notificationDTO.getId());
        notification.setMessage(notificationDTO.getMessage());
        notification.setSenderImage(notificationDTO.getNotificationSenderImage());
        notification.setSenderName(notificationDTO.getNotificationSenderName());
        notification.setTime(notificationDTO.getTime());
        notification.setUserId(notificationDTO.getUserId());
        return notification;
    } 

    public NotificationDTO toDTO() {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setId(id);
        notificationDTO.setMessage(message);
        notificationDTO.setNotificationSenderImage(senderImage);
        notificationDTO.setNotificationSenderName(senderName);
        notificationDTO.setTime(time);
        notificationDTO.setUserId(userId);
        return notificationDTO;
    }
}
