package com.chambaya.backend.jobs.application.services;

import com.chambaya.backend.jobs.application.commads.CreateJobCommand;
import org.springframework.stereotype.Service;
import com.chambaya.backend.jobs.domain.model.Job;
import com.chambaya.backend.jobs.domain.model.JobStatus;
import com.chambaya.backend.jobs.domain.model.Location;
import com.chambaya.backend.jobs.domain.repositories.JobRepository;
import com.chambaya.backend.iam.application.services.UserApplicationService;
import com.chambaya.backend.iam.domain.model.User;
import com.chambaya.backend.iam.domain.model.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class JobApplicationService {

    private final JobRepository jobRepository;
    private final UserApplicationService userApplicationService;
    public JobApplicationService(
            JobRepository jobRepository,
            UserApplicationService userApplicationService
    ) {
        this.jobRepository = jobRepository;
        this.userApplicationService = userApplicationService;
    }

    public Job createJob(CreateJobCommand command){
        validateContractor(command.contractorId());

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

    public Job startJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.start();
        return jobRepository.save(job);
    }

    public Job completeJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.complete();
        return jobRepository.save(job);
    }

    public Job cancelJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.cancel();
        return jobRepository.save(job);
    }

    public Job closeJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.close();

        return jobRepository.save(job);
    }
    public Job reopenJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.reopen();

        return jobRepository.save(job);
    }

    public void matchJob(String id){
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        job.match();
        jobRepository.save(job);
    }

    private void validateContractor(String contractorId){
        User contractor = userApplicationService.findById(contractorId)
                .orElseThrow(() -> new IllegalArgumentException("Contractor not found"));
        if (contractor.getRole() != UserRole.CONTRATANTE) {
            throw new IllegalArgumentException("User is not a contractor");
        }
    }

    public List<Job> findNearbyAvailableJobs(double latitude, double longitude, double radiusKm){
        if (radiusKm <= 0) {
            throw new IllegalArgumentException("Radius must be greater than 0");
        }
        return jobRepository.findAll()
                .stream()
                .filter(job -> job.getStatus() == JobStatus.PUBLISHED || job.getStatus() == JobStatus.REOPENED)
                .filter(job -> job.getLocation() != null)
                .filter(job -> calculateDistanceInKm(
                        latitude,
                        longitude,
                        job.getLocation().getLatitude(),
                        job.getLocation().getLongitude()
                )<= radiusKm)
                .toList();
    }
    private double calculateDistanceInKm(
            double originLatitude,
            double originLongitude,
            double destinationLatitude,
            double destinationLongitude
    ){
        final int earthRadiusKm = 6371;
        double latDistance = Math.toRadians(destinationLatitude - originLatitude);
        double lonDistance = Math.toRadians(destinationLongitude - originLongitude);

        double originLatitudeRadians = Math.toRadians(originLatitude);
        double destinationLatitudeRadians = Math.toRadians(destinationLatitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(originLatitudeRadians)
                * Math.cos(destinationLatitudeRadians)
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadiusKm * c;
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
