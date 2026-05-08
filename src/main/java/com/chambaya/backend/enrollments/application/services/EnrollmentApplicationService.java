package com.chambaya.backend.enrollments.application.services;

import com.chambaya.backend.enrollments.application.commands.AcceptEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.ApplyToJobCommand;
import com.chambaya.backend.enrollments.application.commands.CancelEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.RejectEnrollmentCommand;
import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;
import com.chambaya.backend.enrollments.domain.repositories.EnrollmentRepository;
import com.chambaya.backend.iam.application.services.UserApplicationService;
import com.chambaya.backend.jobs.application.services.JobApplicationService;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.domain.model.UserRole;
import com.chambaya.backend.jobs.domain.model.Job;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class EnrollmentApplicationService {

    private final EnrollmentRepository enrollmentRepository;
    private final JobApplicationService jobApplicationService;
    private final UserApplicationService userApplicationService;
    public EnrollmentApplicationService(
            EnrollmentRepository enrollmentRepository,
            JobApplicationService jobApplicationService,
            UserApplicationService userApplicationService
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.jobApplicationService = jobApplicationService;
        this.userApplicationService = userApplicationService;
    }

    public Enrollment applyToJob(ApplyToJobCommand command){
        Job job = jobApplicationService.findById(command.jobId())
                .orElseThrow(() -> new IllegalArgumentException("Job not found."));
        validateWorker(command.workerId());
        validateContractor(command.contractorId());
        validateContractorOwnsJob(job, command.contractorId());

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
        Enrollment acceptedEnrollment = enrollmentRepository.save(enrollment);
        rejectOtherPendingEnrollments(acceptedEnrollment);
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
    private void rejectOtherPendingEnrollments(Enrollment acceptedEnrollment){
        List<Enrollment> enrollments = enrollmentRepository.findByJobId(acceptedEnrollment.getJobId());
        enrollments.stream()
                .filter(enrollment -> !enrollment.getId().equals(acceptedEnrollment.getId()))
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.PENDING)
                .forEach(enrollment -> {
                    enrollment.reject();
                    enrollmentRepository.save(enrollment);
                });
    }
    private void validateWorker(String workerId){
        User worker = userApplicationService.findById(workerId)
                .orElseThrow(() -> new IllegalArgumentException("Worker not found."));
        if (worker.getRole() != UserRole.CHAMBEADOR) {
            throw new IllegalArgumentException("User is not a worker.");
        }
    }
    private void validateContractor(String contractorId){
        User contractor = userApplicationService.findById(contractorId)
                .orElseThrow(() -> new IllegalArgumentException("Contractor not found."));
        if (contractor.getRole() != UserRole.CONTRATANTE) {
            throw new IllegalArgumentException("User is not a contractor.");
        }
    }
    private void validateContractorOwnsJob(Job job, String contractorId){
        if (!job.getContractorId().equals(contractorId)) {
            throw new IllegalArgumentException("Contractor does not own this job.");
        }
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
