package com.chambaya.backend.jobs.interfaces.rest.resources;

import com.chambaya.backend.jobs.domain.model.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record JobResource(
        String id,
        String contractorId,
        String title,
        String description,
        String category,
        List<String> requiredSkills,
        BigDecimal paymentAmount,
        LocationResource location,
        LocalDateTime scheduleStart,
        LocalDateTime scheduleEnd,
        JobStatus status,
        LocalDateTime creatAt,
        LocalDateTime updatedAt
) {
}
