package com.chambaya.backend.enrollments.application.services;

import com.chambaya.backend.enrollments.application.commands.AcceptEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.ApplyToJobCommand;
import com.chambaya.backend.enrollments.application.commands.CancelEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.RejectEnrollmentCommand;
import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;
import com.chambaya.backend.enrollments.domain.repositories.EnrollmentRepository;
import com.chambaya.backend.jobs.application.services.JobApplicationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class EnrollmentApplicationService {

    private final EnrollmentRepository enrollmentRepository;
    private final JobApplicationService jobApplicationService;
    public EnrollmentApplicationService(
            EnrollmentRepository enrollmentRepository,
            JobApplicationService jobApplicationService
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.jobApplicationService = jobApplicationService;
    }

    public Enrollment applyToJob(ApplyToJobCommand command){
        if (enrollmentRepository.existsByJobIdAndWorkerId(command.jobId(), command.workerId())) {
            throw new IllegalArgumentException("Worker has already applied to this job.");
        }

        Enrollment enrollment = new Enrollment(
                null,
                command.jobId(),
                command.workerId(),
                command.contractorId(),
                EnrollmentStatus.PENDING,
                LocalDateTime.now(),
                null,
                LocalDateTime.now()
        );
        return enrollmentRepository.save(enrollment);
    }
    public Enrollment acceptEnrollment(AcceptEnrollmentCommand command){
        Enrollment enrollment = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));
        enrollment.accept();
        jobApplicationService.matchJob(enrollment.getJobId());
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment rejectEnrollment(RejectEnrollmentCommand command){
        Enrollment enrollment = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));
        enrollment.reject();
        return enrollmentRepository.save(enrollment);
    }
    public Enrollment cancelEnrollment(CancelEnrollmentCommand command){
        Enrollment enrollment = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));
        enrollment.cancel();
        return enrollmentRepository.save(enrollment);
    }

    public Optional<Enrollment> findById(String id){
        return enrollmentRepository.findById(id);
    }
    public List<Enrollment> findAll(){
        return enrollmentRepository.findAll();
    }
    public List<Enrollment> findByJobId(String jobId){
        return enrollmentRepository.findByJobId(jobId);
    }
    public List<Enrollment> findByWorkerId(String workerId){
        return enrollmentRepository.findByWorkerId(workerId);
    }
    public List<Enrollment> findByContractorId(String contractorId){
        return enrollmentRepository.findByContractorId(contractorId);
    }
    public List<Enrollment> findPendingEnrollments(){
        return enrollmentRepository.findByStatus(EnrollmentStatus.PENDING);
    }

}
