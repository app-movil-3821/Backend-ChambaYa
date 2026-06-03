package com.chambaya.backend.payments.application.services;

import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;
import com.chambaya.backend.enrollments.domain.repositories.EnrollmentRepository;
import com.chambaya.backend.jobs.application.services.JobApplicationService;
import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.domain.model.JobStatus;
import com.chambaya.backend.payments.application.commands.CreatePaymentCommand;
import com.chambaya.backend.payments.domain.model.Payment;
import com.chambaya.backend.payments.domain.model.PaymentStatus;
import com.chambaya.backend.payments.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentApplicationService {
    private final PaymentRepository paymentRepository;
    private final JobApplicationService jobApplicationService;
    private final EnrollmentRepository enrollmentRepository;

    public PaymentApplicationService(
            PaymentRepository paymentRepository,
            JobApplicationService jobApplicationService,
            EnrollmentRepository enrollmentRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.jobApplicationService = jobApplicationService;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Payment createPayment(CreatePaymentCommand command) {
        Job job = jobApplicationService.findById(command.jobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (job.getStatus() != JobStatus.COMPLETED) {
            throw new IllegalStateException("Payments can only be created for completed jobs");
        }

        if (paymentRepository.existsByJobId(command.jobId())) {
            throw new IllegalArgumentException("Payment already exists for this job");
        }

        Enrollment acceptedEnrollment = findAcceptedEnrollmentByJobId(command.jobId());

        Payment payment = new Payment(
                null,
                job.getId(),
                acceptedEnrollment.getId(),
                job.getContractorId(),
                acceptedEnrollment.getWorkerId(),
                job.getPaymentAmount(),
                command.method(),
                PaymentStatus.PENDING,
                LocalDateTime.now(),
                null,
                null
        );

        return paymentRepository.save(payment);
    }

    public Payment confirmPayment(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.confirm();

        return paymentRepository.save(payment);
    }

    public Payment cancelPayment(String id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));

        payment.cancel();

        return paymentRepository.save(payment);
    }

    public Optional<Payment> findById(String id) {
        return paymentRepository.findById(id);
    }

    public Optional<Payment> findByJobId(String jobId) {
        return paymentRepository.findByJobId(jobId);
    }

    public List<Payment> findByWorkerId(String workerId) {
        return paymentRepository.findByWorkerId(workerId);
    }

    public List<Payment> findByContractorId(String contractorId) {
        return paymentRepository.findByContractorId(contractorId);
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    private Enrollment findAcceptedEnrollmentByJobId(String jobId) {
        return enrollmentRepository.findByJobId(jobId)
                .stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.ACCEPTED)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Accepted enrollment not found for this job"));
    }
}
