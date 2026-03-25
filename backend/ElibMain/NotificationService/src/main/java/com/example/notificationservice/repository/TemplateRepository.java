package com.example.notificationservice.repository;

import com.example.notificationservice.entity.MessageTemplate;
import com.example.notificationservice.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TemplateRepository extends JpaRepository<MessageTemplate, UUID> {
    Optional<MessageTemplate> findByType(NotificationType type);
}
