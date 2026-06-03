package com.chambaya.backend.jobs.interfaces.rest.controllers;

import com.chambaya.backend.jobs.application.services.JobApplicationService;
import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.interfaces.rest.assemblers.JobResourceAssembler;
import com.chambaya.backend.jobs.interfaces.rest.resources.CreateJobResource;
import com.chambaya.backend.jobs.interfaces.rest.resources.JobResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobApplicationService jobApplicationService;

    public JobController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResource createJob(@Valid @RequestBody CreateJobResource resource){
        Job job = jobApplicationService.createJob(
                JobResourceAssembler.toCreatJobCommand(resource)
        );
        return JobResourceAssembler.toResource(job);
    }

    @GetMapping
    public List<JobResource> getAllJobs(){
        return jobApplicationService.findAll()
                .stream()
                .map(JobResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/nearby")
    public List<JobResource> getNearbyJobs(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusKm,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) BigDecimal minPayment,
            @RequestParam(required = false) BigDecimal maxPayment,
            @RequestParam(required = false) LocalDate scheduledDate
    ) {
        return jobApplicationService.findNearbyAvailableJobs(
                        latitude,
                        longitude,
                        radiusKm,
                        category,
                        district,
                        minPayment,
                        maxPayment,
                        scheduledDate
                )
                .stream()
                .map(JobResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/{id}")
    public JobResource getJobById(@PathVariable String id){
        Job job = jobApplicationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found"));
        return JobResourceAssembler.toResource(job);
    }
    @GetMapping("/contractor/{contractorId}")
    public List<JobResource> getJobsByContractorId(@PathVariable String contractorId){
        return jobApplicationService.findByContractorId(contractorId)
                .stream()
                .map(JobResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/published")
    public List<JobResource> getPublishedJobs(){
        return jobApplicationService.findPublishedJobs()
                .stream()
                .map(JobResourceAssembler::toResource)
                .toList();

    }

    @PutMapping("/{id}/start")
    public JobResource startJob(@PathVariable String id){
        Job job = jobApplicationService.startJob(id);
        return JobResourceAssembler.toResource(job);
    }

    @PutMapping("/{id}/complete")
    public JobResource completeJob(@PathVariable String id){
        Job job = jobApplicationService.completeJob(id);
        return JobResourceAssembler.toResource(job);
    }
    @PutMapping("/{id}/cancel")
    public JobResource cancelJob(@PathVariable String id){
        Job job = jobApplicationService.cancelJob(id);
        return JobResourceAssembler.toResource(job);
    }

    @PutMapping("/{id}/publish")
    public JobResource publishJob(@PathVariable String id){
        Job job = jobApplicationService.publishJob(id);
        return JobResourceAssembler.toResource(job);
    }
    @PutMapping("/{id}/close")
    public JobResource closeJob(@PathVariable String id){
        Job job = jobApplicationService.closeJob(id);
        return JobResourceAssembler.toResource(job);
    }
    @PutMapping("/{id}/reopen")
    public JobResource reopenJob(@PathVariable String id){
        Job job = jobApplicationService.reopenJob(id);
        return JobResourceAssembler.toResource(job);
    }


}
