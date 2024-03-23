package com.vicky.blog.notificationservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vicky.blog.notificationservice.model.Notification;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    List<Notification> findByUserId(String userId);

    Optional<Notification> findByIdAndUserId(String id, String userId);
}
