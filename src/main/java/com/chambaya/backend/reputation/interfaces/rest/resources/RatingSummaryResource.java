package com.chambaya.backend.reputation.interfaces.rest.resources;

public record RatingSummaryResource(
        String userId,
        double averageRating,
        int totalRatings
) {
}
