package com.vicky.blog.notificationservice.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vicky.blog.common.dto.notification.NotificationDTO;
import com.vicky.blog.common.dto.notification.NotificationResponse;
import com.vicky.blog.common.dto.notification.NotificationsResponse;
import com.vicky.blog.common.exception.AppException;
import com.vicky.blog.common.service.NotificationService;
import com.vicky.blog.common.utility.UserIdExtracter;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserIdExtracter userIdExtracter;

    @PostMapping
    public ResponseEntity<NotificationResponse> addNotification(@RequestBody NotificationDTO notificationDTO, 
        Principal principal, HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        NotificationDTO notification = notificationService.addNotification(userId, notificationDTO);
        
        NotificationResponse response = new NotificationResponse();
        response.setMessage("Added notification!");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setNotification(notification);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping
    public ResponseEntity<NotificationsResponse> getNotificationsOfUser(Principal principal, 
        HttpServletRequest request) throws AppException {

        String userId = userIdExtracter.getUserId(principal);
        List<NotificationDTO> notificationDTOs = notificationService.getNotificationsOfUser(userId);

        NotificationsResponse response = new NotificationsResponse();
        response.setMessage("success");
        response.setStatus(HttpStatus.SC_OK);
        response.setPath(request.getServletPath());
        response.setTime(LocalDateTime.now());
        response.setNotifications(notificationDTOs);

        return ResponseEntity.ok().body(response);
    }
}
