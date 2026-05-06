package com.chambaya.backend.reputation.interfaces.rest.resources;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReviewResource(
        @NotBlank
        String jobId,
        @NotBlank
        String reviewerId,
        @NotBlank
        String reviewedUserId,
        @NotNull
        @Min(1)
        @Max(5)
        Integer rating,
        String comment

) {
}
