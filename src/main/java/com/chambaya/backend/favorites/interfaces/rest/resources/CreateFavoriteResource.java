package com.chambaya.backend.favorites.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateFavoriteResource(
        @NotBlank
        String workerId,
        @NotBlank
        String jobId
) {
}
