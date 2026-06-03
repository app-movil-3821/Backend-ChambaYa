package com.chambaya.backend.favorites.interfaces.rest.resources;

import java.time.LocalDateTime;

public record FavoriteResource(
        String id,
        String workerId,
        String jobId,
        LocalDateTime createdAt
) {
}
