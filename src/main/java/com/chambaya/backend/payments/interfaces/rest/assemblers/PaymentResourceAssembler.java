package com.chambaya.backend.payments.interfaces.rest.assemblers;

import com.chambaya.backend.payments.application.commands.CreatePaymentCommand;
import com.chambaya.backend.payments.domain.model.Payment;
import com.chambaya.backend.payments.interfaces.rest.resources.CreatePaymentResource;
import com.chambaya.backend.payments.interfaces.rest.resources.PaymentResource;

public class PaymentResourceAssembler {
    private PaymentResourceAssembler() {
    }

    public static PaymentResource toResource(Payment payment) {
        return new PaymentResource(
                payment.getId(),
                payment.getJobId(),
                payment.getEnrollmentId(),
                payment.getContractorId(),
                payment.getWorkerId(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getConfirmedAt(),
                payment.getCancelledAt()
        );
    }

    public static CreatePaymentCommand toCreatePaymentCommand(CreatePaymentResource resource) {
        return new CreatePaymentCommand(
                resource.jobId(),
                resource.method()
        );
    }
}
