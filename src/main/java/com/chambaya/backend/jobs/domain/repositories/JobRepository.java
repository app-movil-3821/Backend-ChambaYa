package com.chambaya.backend.jobs.domain.repositories;

import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.domain.model.JobStatus;


import java.util.List;
import java.util.Optional;

public interface JobRepository {

    Job save(Job job);
    Optional<Job> findById(String id);
    List<Job> findAll();
    List<Job> findByContractorId(String contractorId);
    List<Job> findByStatus(JobStatus status);
    void deleteById(String id);
}
