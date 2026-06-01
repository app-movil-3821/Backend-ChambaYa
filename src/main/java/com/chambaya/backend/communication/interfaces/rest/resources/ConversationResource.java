package com.chambaya.backend.communication.interfaces.rest.resources;

import com.chambaya.backend.communication.domain.model.ConversationStatus;

import java.time.LocalDateTime;

public record ConversationResource(
        String id,
        String jobId,
        String enrollmentId,
        String contractorId,
        String workerId,
        ConversationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
