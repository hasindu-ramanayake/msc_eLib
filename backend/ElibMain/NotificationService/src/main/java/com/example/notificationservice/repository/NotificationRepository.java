package com.example.notificationservice.repository;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<Notification> findByUserIdAndStatus(UUID userId, NotificationStatus status);
}