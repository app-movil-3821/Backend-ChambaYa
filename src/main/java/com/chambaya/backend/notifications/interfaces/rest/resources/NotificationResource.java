package com.chambaya.backend.notifications.interfaces.rest.resources;

import com.chambaya.backend.notifications.domain.model.NotificationType;

import java.time.LocalDateTime;

public record NotificationResource(
        String id,
        String userId,
        String title,
        String message,
        NotificationType type,
        boolean read,
        LocalDateTime createdAt,
        LocalDateTime readAt
) {
}
