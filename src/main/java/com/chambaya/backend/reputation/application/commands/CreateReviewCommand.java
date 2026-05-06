package com.chambaya.backend.reputation.application.commands;

public record CreateReviewCommand(
        String jobId,
        String reviewerId,
        String reviewedUserId,
        int rating,
        String comment
) {
}
