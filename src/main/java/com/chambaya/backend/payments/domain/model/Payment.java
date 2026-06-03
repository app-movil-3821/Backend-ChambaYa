package com.chambaya.backend.payments.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {

    @Id
    private String id;

    private String jobId;
    private String enrollmentId;
    private String contractorId;
    private String workerId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime confirmedAt;
    private LocalDateTime cancelledAt;

    public void confirm() {
        if (this.status != PaymentStatus.PENDING) {
            throw new IllegalStateException("Only pending payments can be confirmed");
        }

        this.status = PaymentStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status == PaymentStatus.CONFIRMED) {
            throw new IllegalStateException("Confirmed payments cannot be cancelled");
        }

        this.status = PaymentStatus.CANCELLED;
        this.cancelledAt = LocalDateTime.now();
    }
}
