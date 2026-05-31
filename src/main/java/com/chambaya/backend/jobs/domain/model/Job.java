package com.chambaya.backend.jobs.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Job {

    private String id;
    private String contractorId;
    private String title;
    private String description;
    private String category;
    private List<String> requiredSkills;
    private BigDecimal paymentAmount;
    private Location location;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void publish(){
        this.status = JobStatus.PUBLISHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void match(){
        this.status = JobStatus.MATCHED;
        this.updatedAt = LocalDateTime.now();
    }

    public void start(){
        if (this.status != JobStatus.MATCHED){
            throw new IllegalStateException("Only matched jobs can be started.");
        }

        this.status = JobStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete(){
        if (this.status != JobStatus.IN_PROGRESS){
            throw new IllegalStateException("Only in progress jobs can be completed.");
        }

        this.status = JobStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }

    public void cancel(){
        if (this.status == JobStatus.COMPLETED){
            throw new IllegalStateException("Completed jobs cannot be cancelled.");
        }

        this.status = JobStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }

    public void close(){
        this.status = JobStatus.CLOSED;
        this.updatedAt = LocalDateTime.now();
    }

    public void reopen(){
        this.status = JobStatus.REOPENED;
        this.updatedAt = LocalDateTime.now();
    }
}
