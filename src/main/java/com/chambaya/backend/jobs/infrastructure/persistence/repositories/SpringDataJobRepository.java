package com.chambaya.backend.jobs.infrastructure.persistence.repositories;

import com.chambaya.backend.jobs.domain.model.JobStatus;
import com.chambaya.backend.jobs.infrastructure.persistence.documents.JobDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataJobRepository extends MongoRepository<JobDocument, String> {

    List<JobDocument> findByContractorId(String contractorId);
    List<JobDocument> findByStatus(JobStatus status);
}
