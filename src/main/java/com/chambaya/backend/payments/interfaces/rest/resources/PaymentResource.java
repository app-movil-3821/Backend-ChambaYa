package com.chambaya.backend.payments.interfaces.rest.resources;

import com.chambaya.backend.payments.domain.model.PaymentMethod;
import com.chambaya.backend.payments.domain.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResource(
        String id,
        String jobId,
        String enrollmentId,
        String contractorId,
        String workerId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime createdAt,
        LocalDateTime confirmedAt,
        LocalDateTime cancelledAt
) {
}
