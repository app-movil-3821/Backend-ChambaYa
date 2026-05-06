package com.chambaya.backend.enrollments.domain.repositories;

import com.chambaya.backend.enrollments.domain.model.Enrollment;
import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {

    Enrollment save(Enrollment enrollment);
    Optional<Enrollment> findById(String id);
    List<Enrollment> findAll();
    List<Enrollment> findByJobId(String jobId);
    List<Enrollment> findByWorkerId(String workerId);
    List<Enrollment> findByContractorId(String contractorId);
    List<Enrollment> findByStatus(EnrollmentStatus status);
    boolean existsByJobIdAndWorkerId(String jobId, String workerId);
    void deleteById(String id);
}
