package com.chambaya.backend.notifications.application.commands;

import com.chambaya.backend.notifications.domain.model.NotificationType;

public record CreateNotificationCommand(
        String userId,
        String title,
        String message,
        NotificationType type
) {
}
