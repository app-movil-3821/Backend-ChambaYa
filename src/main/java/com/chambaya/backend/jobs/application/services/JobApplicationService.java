package com.chambaya.backend.jobs.application.services;

import com.chambaya.backend.jobs.application.commads.CreateJobCommand;
import org.springframework.stereotype.Service;
import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.domain.model.JobStatus;
import com.chambaya.backend.jobs.domain.model.Location;
import com.chambaya.backend.jobs.domain.repositories.JobRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class JobApplicationService {

    private final JobRepository jobRepository;
    public JobApplicationService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(CreateJobCommand command){
        Location location = new Location(
                command.latitude(),
                command.longitude(),
                command.address(),
                command.district()
        );

        Job job = new Job(
                null,
                command.contractorId(),
                command.title(),
                command.description(),
                command.category(),
                command.requiredSkills(),
                command.paymentAmount(),
                location,
                command.scheduledStrat(),
                command.scheduledEnd(),
                JobStatus.PUBLISHED,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        return jobRepository.save(job);
    }

    public Job publishJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.publish();

        return jobRepository.save(job);
    }

    public Job closeJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.publish();

        return jobRepository.save(job);
    }
    public Job reopenJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.publish();

        return jobRepository.save(job);
    }

    public Optional<Job> findById(String id){
        return jobRepository.findById(id);
    }
    public List<Job> findAll(){
        return jobRepository.findAll();
    }
    public List<Job> findByContractorId(String contractorId){
        return jobRepository.findByContractorId(contractorId);
    }
    public List<Job> findPublishedJobs(){
        return jobRepository.findByStatus(JobStatus.PUBLISHED);
    }
}
