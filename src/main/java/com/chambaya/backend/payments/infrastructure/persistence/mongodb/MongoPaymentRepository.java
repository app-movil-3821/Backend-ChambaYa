package com.chambaya.backend.payments.infrastructure.persistence.mongodb;

import com.chambaya.backend.payments.domain.model.Payment;
import com.chambaya.backend.payments.domain.repositories.PaymentRepository;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoPaymentRepository extends MongoRepository<Payment, String>, PaymentRepository {
}
