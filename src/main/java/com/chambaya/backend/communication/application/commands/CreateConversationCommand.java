package com.chambaya.backend.communication.application.commands;

public record CreateConversationCommand(
        String jobId,
        String enrollmentId,
        String contractorId,
        String workerId
) {
}
