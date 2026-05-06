package com.chambaya.backend.iam.interfaces.rest.resources;

import com.chambaya.backend.iam.domain.model.UserRole;

import java.time.LocalDateTime;

public record UserResource(
        String id,
        String name,
        String email,
        UserRole role,
        ProfileResource profile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
