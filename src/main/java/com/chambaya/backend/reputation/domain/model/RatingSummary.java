package com.chambaya.backend.reputation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RatingSummary {
    private String userId;
    private double averageRating;
    private int totalRatings;
}
