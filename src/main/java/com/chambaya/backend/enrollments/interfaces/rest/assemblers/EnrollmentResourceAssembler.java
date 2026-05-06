package com.chambaya.backend.enrollments.interfaces.rest.assemblers;

import com.chambaya.backend.enrollments.application.commands.AcceptEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.ApplyToJobCommand;
import com.chambaya.backend.enrollments.application.commands.CancelEnrollmentCommand;
import com.chambaya.backend.enrollments.application.commands.RejectEnrollmentCommand;
import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.interfaces.rest.resources.ApplyToJobResource;
import com.chambaya.backend.enrollments.interfaces.rest.resources.EnrollmentResource;

public class EnrollmentResourceAssembler {
    private EnrollmentResourceAssembler() {}
    public static ApplyToJobCommand toApplyToJobCommand(ApplyToJobResource resource){
        return new ApplyToJobCommand(
                resource.jobId(),
                resource.workerId(),
                resource.contractorId()
        );
    }
    public static AcceptEnrollmentCommand toAcceptEnrollmentCommand(String enrollmentId){
        return new AcceptEnrollmentCommand(enrollmentId);
    }
    public static RejectEnrollmentCommand toRejectEnrollmentCommand(String enrollmentId){
        return new RejectEnrollmentCommand(enrollmentId);
    }
    public static CancelEnrollmentCommand toCancelEnrollmentCommand(String enrollmentId){
        return new CancelEnrollmentCommand(enrollmentId);
    }
    public static EnrollmentResource toResource(Enrollment enrollment){
        return new EnrollmentResource(
                enrollment.getId(),
                enrollment.getJobId(),
                enrollment.getWorkerId(),
                enrollment.getContractorId(),
                enrollment.getStatus(),
                enrollment.getAppliedAt(),
                enrollment.getDecidedAt(),
                enrollment.getUpdatedAt()
        );
    }
}
