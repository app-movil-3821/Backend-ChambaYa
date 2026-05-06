package com.chambaya.backend.reputation.interfaces.rest.resources;

import java.time.LocalDateTime;

public record ReviewResource(
        String id,
        String jobId,
        String reviewerId,
        String reviewedUserId,
        int rating,
        String comment,
        LocalDateTime createdAt
) {
}
