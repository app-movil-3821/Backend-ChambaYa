package com.chambaya.backend.payments.interfaces.rest.resources;

import com.chambaya.backend.payments.domain.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentResource(
        @NotBlank
        String jobId,
        @NotNull
        PaymentMethod method
) {
}
