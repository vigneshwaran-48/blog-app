package com.vicky.blog.common.service;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.exception.AppException;

public interface NotificationService {
    
    NotificationDTO addNotification(String userId, NotificationDTO notification) throws AppException;
}
