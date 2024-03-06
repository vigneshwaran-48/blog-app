package com.vicky.blog.notificationservice.service;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.user.UserResponseData;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.NotificationService;
import com.vicky.blog.notificationservice.client.UserServiceClient;
import com.vicky.blog.notificationservice.model.Notification;
import com.vicky.blog.notificationservice.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public NotificationDTO addNotification(String userId, NotificationDTO notification) throws AppException {
        UserResponseData userResponse = userServiceClient.getUser(userId);
        if(userResponse.getStatus() != HttpStatus.SC_OK) {
            throw new AppException(userResponse.getStatus(), userResponse.getMessage());
        }
        Notification notificationModel = Notification.build(notification);
        Notification savedNotification = notificationRepository.save(notificationModel);
        if(savedNotification == null) {
            throw new AppException("Error while adding notification");
        }
        return savedNotification.toDTO();
    }
    
}
