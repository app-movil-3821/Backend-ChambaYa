package com.chambaya.backend.communication.interfaces.rest.resources;

import java.time.LocalDateTime;

public record MessageResource(
        String id,
        String conversationId,
        String senderId,
        String content,
        LocalDateTime sentAt,
        boolean read,
        LocalDateTime readAt
) {
}
