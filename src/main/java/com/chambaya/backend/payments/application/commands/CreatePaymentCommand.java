package com.chambaya.backend.payments.application.commands;

import com.chambaya.backend.payments.domain.model.PaymentMethod;

public record CreatePaymentCommand(
        String jobId,
        PaymentMethod method
) {
}
