package com.chambaya.backend.enrollments.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record ApplyToJobResource(
        @NotBlank
        String jobId,
        @NotBlank
        String workerId,
        @NotBlank
        String contractorId
) {
}
