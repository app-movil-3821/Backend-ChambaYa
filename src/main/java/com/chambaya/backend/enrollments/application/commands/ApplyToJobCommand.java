package com.chambaya.backend.enrollments.application.commands;

public record ApplyToJobCommand(
        String jobId,
        String workerId,
        String contractorId
) {
}
