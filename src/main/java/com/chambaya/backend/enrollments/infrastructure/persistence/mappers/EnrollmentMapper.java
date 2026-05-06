package com.chambaya.backend.enrollments.infrastructure.persistence.mappers;

import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.infrastructure.persistence.documents.EnrollmentDocument;

public class EnrollmentMapper {
    private EnrollmentMapper() {}
    public static EnrollmentDocument toDocument(Enrollment enrollment){
        if (enrollment == null) {
            return null;
        }
        return new EnrollmentDocument(
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
    public static Enrollment toDomain(EnrollmentDocument document){
        if (document == null) {
            return null;
        }
        return new Enrollment(
                document.getId(),
                document.getJobId(),
                document.getWorkerId(),
                document.getContractorId(),
                document.getStatus(),
                document.getAppliedAt(),
                document.getDecidedAt(),
                document.getUpdatedAt()
        );
    }
}
