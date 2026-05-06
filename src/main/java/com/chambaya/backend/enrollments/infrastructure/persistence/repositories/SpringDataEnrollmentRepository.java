package com.chambaya.backend.enrollments.infrastructure.persistence.repositories;

import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;
import com.chambaya.backend.enrollments.infrastructure.persistence.documents.EnrollmentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataEnrollmentRepository extends MongoRepository<EnrollmentDocument, String> {

    List<EnrollmentDocument> findByJobId(String jobId);
    List<EnrollmentDocument> findByWorkerId(String workerId);
    List<EnrollmentDocument> findByContractorId(String contractorId);
    List<EnrollmentDocument> findByStatus(EnrollmentStatus status);
    boolean existsByJobIdAndWorkerId(String jobId, String workerId);

}
