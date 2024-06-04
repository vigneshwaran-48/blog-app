package com.vicky.blog.notificationservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationDTO.NotificationSenderType;
import com.vicky.blog.common.dto.organization.OrganizationDTO;
import com.vicky.blog.common.dto.user.UserDTO;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.NotificationService;
import com.vicky.blog.common.service.OrganizationService;
import com.vicky.blog.common.service.UserService;
import com.vicky.blog.notificationservice.model.Notification;
import com.vicky.blog.notificationservice.repository.NotificationRepository;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Override
    public NotificationDTO addNotification(String userId, NotificationDTO notification) throws AppException {
        
        Optional<UserDTO> user = userService.getUser(userId);
        if(user.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Current user not exists!");
        }
        Optional<UserDTO> userToNotify = userService.getUser(notification.getUserId());
        if(userToNotify.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "User " + notification.getUserId() + " not exists!");
        }

        String senderId = user.get().getId();
        String senderName = user.get().getName();
        String senderImage = user.get().getImage();

        if(notification.getSenderType() == NotificationSenderType.ORGANIZATION) {
            String organizationId = notification.getOrganizationId();
            if(organizationId == null) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, "Organization Id is required!");
            }
            if(!organizationService.isUserHasAccessToNotification(userId, organizationId)) {
                throw new AppException(HttpStatus.SC_FORBIDDEN, 
                    "You don't have permission to send notification on behalf of the organization");
            }
            Optional<OrganizationDTO> organizationDTO = 
                organizationService.getOrganization(userId, organizationId);
            if(organizationDTO.isEmpty()) {
                throw new AppException(HttpStatus.SC_BAD_REQUEST, "Organization " + organizationId + " not exists!");
            }
            senderId = organizationDTO.get().getId().toString();
            senderName = organizationDTO.get().getName();
            senderImage = organizationDTO.get().getImage();
        }
        
        notification.setSenderId(senderId);
        notification.setSenderImage(senderImage);
        notification.setSenderName(senderName);

        Notification notificationModel = Notification.build(notification);
        notificationModel.setTime(LocalDateTime.now());
        notificationModel.setSeen(false);
        
        Notification savedNotification = notificationRepository.save(notificationModel);
        if(savedNotification == null) {
            throw new AppException("Error while adding notification");
        }
        return savedNotification.toDTO();
    }

    @Override
    public List<NotificationDTO> getNotificationsOfUser(String userId) throws AppException {
        Optional<UserDTO> user = userService.getUser(userId);
        if(user.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Current user not exists!");
        }
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream().map(notification -> notification.toDTO()).collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String userId, String notificationId) throws AppException {
        Optional<UserDTO> user = userService.getUser(userId);
        if(user.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "User" + userId + " not exists!");
        }
        Optional<Notification> notification = notificationRepository.findByIdAndUserId(notificationId, userId);
        if(notification.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "Notification not exists!");
        }
        notification.get().setSeen(true);
        Notification savedNotification = notificationRepository.save(notification.get());
        if(savedNotification == null) {
            LOGGER.info("Error while marking as read the notification {}", notificationId);
            throw new AppException("Error while marking as read the notification");
        }
    }

    @Override
    public void markAllAsRead(String userId) throws AppException {
        Optional<UserDTO> user = userService.getUser(userId);
        if(user.isEmpty()) {
            throw new AppException(HttpStatus.SC_BAD_REQUEST, "User" + userId + " not exists!");
        }
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        for (Notification notification : notifications) {
            markAsRead(userId, notification.getId());
        }
    }
    
}
