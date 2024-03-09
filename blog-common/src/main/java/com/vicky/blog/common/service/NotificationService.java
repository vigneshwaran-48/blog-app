package com.vicky.blog.common.service;

import java.util.List;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.exception.AppException;

public interface NotificationService {
    
    NotificationDTO addNotification(String userId, NotificationDTO notification) throws AppException;

    List<NotificationDTO> getNotificationsOfUser(String userId) throws AppException;

    void markAsRead(String userId, Long notificationId) throws AppException;
}
