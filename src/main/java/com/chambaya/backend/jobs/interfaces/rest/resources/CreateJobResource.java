package com.chambaya.backend.jobs.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateJobResource(
        @NotBlank
        String contractorId,
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotBlank
        String category,
        List<String> requiredSkills,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal paymentAmount,

        double latitude,
        double longitude,

        @NotBlank
        String address,
        @NotBlank
        String district,
        @NotNull
        LocalDateTime scheduledStart,
        @NotNull
        LocalDateTime scheduledEnd
) {
}
