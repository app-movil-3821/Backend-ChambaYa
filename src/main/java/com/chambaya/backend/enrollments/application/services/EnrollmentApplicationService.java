package com.chambaya.backend.enrollments.application.services;

import com.chambaya.backend.enrollments.application.commands.AcceptEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.ApplyToJobCommand;
import com.chambaya.backend.enrollments.application.commands.CancelEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.RejectEnrollmentCommand;
import com.chambaya.backend.notifications.application.commands.CreateNotificationCommand;
import com.chambaya.backend.notifications.application.services.NotificationApplicationService;
import com.chambaya.backend.communication.application.commands.CreateConversationCommand;
import com.chambaya.backend.communication.application.services.CommunicationApplicationService;
import com.chambaya.backend.notifications.domain.model.NotificationType;
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
    private final NotificationApplicationService notificationApplicationService;

    private final CommunicationApplicationService communicationApplicationService;
    public EnrollmentApplicationService(
            EnrollmentRepository enrollmentRepository,
            JobApplicationService jobApplicationService,
            UserApplicationService userApplicationService,
            NotificationApplicationService notificationApplicationService,
            CommunicationApplicationService communicationApplicationService
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.jobApplicationService = jobApplicationService;
        this.userApplicationService = userApplicationService;
        this.notificationApplicationService = notificationApplicationService;
        this.communicationApplicationService = communicationApplicationService;
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
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        notifyEnrollmentReceived(savedEnrollment, job);

        return savedEnrollment;
    }
    public Enrollment acceptEnrollment(AcceptEnrollmentCommand command){
        Enrollment enrollment = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));
        enrollment.accept();

        jobApplicationService.matchJob(enrollment.getJobId());

        Enrollment acceptedEnrollment = enrollmentRepository.save(enrollment);
        Job job = findJobById(acceptedEnrollment.getJobId());

        createConversationForAcceptedEnrollment(acceptedEnrollment, job);

        notifyEnrollmentAccepted(acceptedEnrollment, job);
        rejectOtherPendingEnrollments(acceptedEnrollment, job);

        return acceptedEnrollment;
    }

    public Enrollment rejectEnrollment(RejectEnrollmentCommand command){
        Enrollment enrollment = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));
        enrollment.reject();

        Enrollment rejectedEnrollment = enrollmentRepository.save(enrollment);
        Job job = findJobById(rejectedEnrollment.getJobId());

        notifyEnrollmentRejected(rejectedEnrollment, job);

        return rejectedEnrollment;
    }
    public Enrollment cancelEnrollment(CancelEnrollmentCommand command){
        Enrollment enrollment = enrollmentRepository.findById(command.enrollmentId())
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found."));
        enrollment.cancel();

        Enrollment cancelledEnrollment = enrollmentRepository.save(enrollment);
        Job job = findJobById(cancelledEnrollment.getJobId());

        notifyEnrollmentCancelled(cancelledEnrollment, job);

        return cancelledEnrollment;
    }
    private void rejectOtherPendingEnrollments(Enrollment acceptedEnrollment, Job job){
        List<Enrollment> enrollments = enrollmentRepository.findByJobId(acceptedEnrollment.getJobId());
        enrollments.stream()
                .filter(enrollment -> !enrollment.getId().equals(acceptedEnrollment.getId()))
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.PENDING)
                .forEach(enrollment -> {
                    enrollment.reject();
                    Enrollment rejectedEnrollment = enrollmentRepository.save(enrollment);
                    notifyEnrollmentRejected(rejectedEnrollment, job);
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

    private Job findJobById(String jobId) {
        return jobApplicationService.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found."));
    }

    private void createConversationForAcceptedEnrollment(Enrollment enrollment, Job job) {
        communicationApplicationService.createConversation(
                new CreateConversationCommand(
                        job.getId(),
                        enrollment.getId(),
                        enrollment.getContractorId(),
                        enrollment.getWorkerId()
                )
        );
    }

    private void notifyEnrollmentReceived(Enrollment enrollment, Job job) {
        notificationApplicationService.creatNotification(
                new CreateNotificationCommand(
                        enrollment.getContractorId(),
                        "Nueva postulación",
                        "Un chambeador postuló al trabajo: " + job.getTitle(),
                        NotificationType.ENROLLMENT_RECEIVED
                )
        );
    }

    private void notifyEnrollmentAccepted(Enrollment enrollment, Job job) {
        notificationApplicationService.creatNotification(
                new CreateNotificationCommand(
                        enrollment.getWorkerId(),
                        "Postulación aceptada",
                        "Tu postulación fue aceptada para el trabajo: " + job.getTitle(),
                        NotificationType.ENROLLMENT_ACCEPTED
                )
        );
    }

    private void notifyEnrollmentRejected(Enrollment enrollment, Job job) {
        notificationApplicationService.creatNotification(
                new CreateNotificationCommand(
                        enrollment.getWorkerId(),
                        "Postulación rechazada",
                        "Tu postulación fue rechazada para el trabajo: " + job.getTitle(),
                        NotificationType.ENROLLMENT_REJECTED
                )
        );
    }

    private void notifyEnrollmentCancelled(Enrollment enrollment, Job job) {
        notificationApplicationService.creatNotification(
                new CreateNotificationCommand(
                        enrollment.getContractorId(),
                        "Postulación cancelada",
                        "Un chambeador canceló su postulación al trabajo: " + job.getTitle(),
                        NotificationType.ENROLLMENT_CANCELLED
                )
        );
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
