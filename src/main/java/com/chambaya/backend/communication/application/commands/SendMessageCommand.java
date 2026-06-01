package com.chambaya.backend.communication.application.commands;

public record SendMessageCommand(
        String conversationId,
        String senderId,
        String content
) {
}
