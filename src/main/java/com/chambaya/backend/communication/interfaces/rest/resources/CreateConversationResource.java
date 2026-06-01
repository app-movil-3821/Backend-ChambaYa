package com.chambaya.backend.communication.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateConversationResource(
        @NotBlank
        String jobId,
        @NotBlank
        String enrollmentId,
        @NotBlank
        String contractorId,
        @NotBlank
        String workerId
) {
}
