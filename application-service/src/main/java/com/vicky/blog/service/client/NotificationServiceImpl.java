package com.vicky.blog.service.client;

import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.client.NotificationServiceClient;
import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationResponse;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationServiceClient notificationServiceClient;

    @Override
    public NotificationDTO addNotification(String userId, NotificationDTO notification) throws AppException {
        NotificationResponse response = notificationServiceClient.addNotification(notification);
        if(response.getStatus() != HttpStatus.SC_OK) {
            throw new AppException(response.getStatus(), response.getMessage());
        }
        return response.getNotification();
    }

    @Override
    public List<NotificationDTO> getNotificationsOfUser(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getNotificationsOfUser'");
    }

    @Override
    public void markAsRead(String userId, String notificationId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markAsRead'");
    }

    @Override
    public void markAllAsRead(String userId) throws AppException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'markAllAsRead'");
    }
    
}
