package com.chambaya.backend.jobs.application.commads;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CreateJobCommand(

        String contractorId,
        String title,
        String description,
        String category,
        List<String> requiredSkills,
        BigDecimal paymentAmount,
        double latitude,
        double longitude,
        String address,
        String district,
        LocalDateTime scheduledStrat,
        LocalDateTime scheduledEnd

) {
}
