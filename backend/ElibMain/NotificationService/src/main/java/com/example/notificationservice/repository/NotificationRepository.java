package com.example.notificationservice.repository;

import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.entity.NotificationChannel;
import com.example.notificationservice.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUserIdAndChannelOrderByCreatedAtDesc(UUID userId, NotificationChannel channel);
    List<Notification> findByUserIdAndStatus(UUID userId, NotificationStatus status);
    @Modifying
    @Query("UPDATE Notification n SET n.status = 'READ' WHERE n.userId = :userId AND n.status = 'SENT' AND n.channel = 'IN_APP'")
    void markAllAsReadForUser(@Param("userId") UUID userId);
}