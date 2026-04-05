package com.example.notificationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "message_templates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private NotificationType type;

    // Subject of the notification, e.g. "Reminder: {{itemTitle}} is due soon"
    @Column(nullable = false)
    private String subject;

    // Body text with {{placeholder}} tokens replaced by TemplateEngine
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;
}