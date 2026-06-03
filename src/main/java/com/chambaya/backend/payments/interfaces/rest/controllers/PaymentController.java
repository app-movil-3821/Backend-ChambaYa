package com.chambaya.backend.payments.interfaces.rest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.chambaya.backend.payments.application.services.PaymentApplicationService;
import com.chambaya.backend.payments.domain.model.Payment;
import com.chambaya.backend.payments.interfaces.rest.assemblers.PaymentResourceAssembler;
import com.chambaya.backend.payments.interfaces.rest.resources.CreatePaymentResource;
import com.chambaya.backend.payments.interfaces.rest.resources.PaymentResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final PaymentApplicationService paymentApplicationService;

    public PaymentController(PaymentApplicationService paymentApplicationService) {
        this.paymentApplicationService = paymentApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResource createPayment(@Valid @RequestBody CreatePaymentResource resource) {
        Payment payment = paymentApplicationService.createPayment(
                PaymentResourceAssembler.toCreatePaymentCommand(resource)
        );

        return PaymentResourceAssembler.toResource(payment);
    }

    @GetMapping
    public List<PaymentResource> getAllPayments() {
        return paymentApplicationService.findAll()
                .stream()
                .map(PaymentResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}")
    public PaymentResource getPaymentById(@PathVariable String id) {
        Payment payment = paymentApplicationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        return PaymentResourceAssembler.toResource(payment);
    }

    @GetMapping("/job/{jobId}")
    public PaymentResource getPaymentByJobId(@PathVariable String jobId) {
        Payment payment = paymentApplicationService.findByJobId(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found"));

        return PaymentResourceAssembler.toResource(payment);
    }

    @GetMapping("/worker/{workerId}")
    public List<PaymentResource> getPaymentsByWorkerId(@PathVariable String workerId) {
        return paymentApplicationService.findByWorkerId(workerId)
                .stream()
                .map(PaymentResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/contractor/{contractorId}")
    public List<PaymentResource> getPaymentsByContractorId(@PathVariable String contractorId) {
        return paymentApplicationService.findByContractorId(contractorId)
                .stream()
                .map(PaymentResourceAssembler::toResource)
                .toList();
    }

    @PutMapping("/{id}/confirm")
    public PaymentResource confirmPayment(@PathVariable String id) {
        Payment payment = paymentApplicationService.confirmPayment(id);

        return PaymentResourceAssembler.toResource(payment);
    }

    @PutMapping("/{id}/cancel")
    public PaymentResource cancelPayment(@PathVariable String id) {
        Payment payment = paymentApplicationService.cancelPayment(id);

        return PaymentResourceAssembler.toResource(payment);
    }


}
