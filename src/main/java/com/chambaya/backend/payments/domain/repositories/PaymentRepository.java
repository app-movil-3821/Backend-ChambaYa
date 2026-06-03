package com.chambaya.backend.payments.domain.repositories;

import com.chambaya.backend.payments.domain.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(String id);

    Optional<Payment> findByJobId(String jobId);

    List<Payment> findByWorkerId(String workerId);

    List<Payment> findByContractorId(String contractorId);

    List<Payment> findAll();

    boolean existsByJobId(String jobId);
}
