package com.chambaya.backend.enrollments.interfaces.rest.controllers;

import com.chambaya.backend.enrollments.application.services.EnrollmentApplicationService;
import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.interfaces.rest.assemblers.EnrollmentResourceAssembler;
import com.chambaya.backend.enrollments.interfaces.rest.resources.ApplyToJobResource;
import com.chambaya.backend.enrollments.interfaces.rest.resources.EnrollmentResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentsController {
    private final EnrollmentApplicationService enrollmentApplicationService;
    public EnrollmentsController(EnrollmentApplicationService enrollmentApplicationService) {
        this.enrollmentApplicationService = enrollmentApplicationService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentResource applyToJob(@Valid @RequestBody ApplyToJobResource resource){
        Enrollment enrollment = enrollmentApplicationService.applyToJob(
                EnrollmentResourceAssembler.toApplyToJobCommand(resource)
        );
        return EnrollmentResourceAssembler.toResource(enrollment);
    }

    @GetMapping
    public List<EnrollmentResource> getAllEnrollments(){
        return enrollmentApplicationService.findAll()
                .stream()
                .map(EnrollmentResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}")
    public EnrollmentResource getEnrollmentById(@PathVariable String id){
        Enrollment enrollment = enrollmentApplicationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enrollment not found"));
        return EnrollmentResourceAssembler.toResource(enrollment);
    }

    @GetMapping("/job/{jobId}")
    public List<EnrollmentResource> getEnrollmentsByJobId(@PathVariable String jobId){
        return enrollmentApplicationService.findByJobId(jobId)
                .stream()
                .map(EnrollmentResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/worker/{workerId}")
    public List<EnrollmentResource> getEnrollmentsByWorkerId(@PathVariable String workerId){
        return enrollmentApplicationService.findByWorkerId(workerId)
                .stream()
                .map(EnrollmentResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/contractor/{contractorId}")
    public List<EnrollmentResource> getEnrollmentsByContractorId(@PathVariable String contractorId){
        return enrollmentApplicationService.findByContractorId(contractorId)
                .stream()
                .map(EnrollmentResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/pending")
    public List<EnrollmentResource> getPendingEnrollments(){
        return enrollmentApplicationService.findPendingEnrollments()
                .stream()
                .map(EnrollmentResourceAssembler::toResource)
                .toList();
    }
    @PutMapping("/{id}/accept")
    public EnrollmentResource acceptEnrollment(@PathVariable String id){
        Enrollment enrollment = enrollmentApplicationService.acceptEnrollment(
                EnrollmentResourceAssembler.toAcceptEnrollmentCommand(id)
        );
        return EnrollmentResourceAssembler.toResource(enrollment);
    }
    @PutMapping("/{id}/reject")
    public EnrollmentResource rejectEnrollment(@PathVariable String id){
        Enrollment enrollment = enrollmentApplicationService.rejectEnrollment(
                EnrollmentResourceAssembler.toRejectEnrollmentCommand(id)
        );
        return EnrollmentResourceAssembler.toResource(enrollment);
    }
    @PutMapping("/{id}/cancel")
    public EnrollmentResource cancelEnrollment(@PathVariable String id){
        Enrollment enrollment = enrollmentApplicationService.cancelEnrollment(
                EnrollmentResourceAssembler.toCancelEnrollmentCommand(id)
        );
        return EnrollmentResourceAssembler.toResource(enrollment);
    }
}
