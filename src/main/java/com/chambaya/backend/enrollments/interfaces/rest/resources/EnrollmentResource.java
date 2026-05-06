package com.chambaya.backend.enrollments.interfaces.rest.resources;

import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;

import java.time.LocalDateTime;

public record EnrollmentResource(
        String id,
        String jobId,
        String workerId,
        String contractorId,
        EnrollmentStatus status,
        LocalDateTime appliedAt,
        LocalDateTime decideAt,
        LocalDateTime updatedAt

) {
}
