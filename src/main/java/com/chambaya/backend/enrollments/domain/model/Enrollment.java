package com.chambaya.backend.enrollments.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

    private String id;
    private String jobId;
    private String workerId;
    private String contractorId;
    private EnrollmentStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime decidedAt;
    private LocalDateTime updatedAt;

    public void accept(){
        if (this.status != EnrollmentStatus.PENDING){
            throw new IllegalStateException("Only pending enrollments can be accepted.");
        }
        this.status = EnrollmentStatus.ACCEPTED;
        this.decidedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    public void reject(){
        if (this.status != EnrollmentStatus.PENDING){
            throw new IllegalStateException("Only pending enrollments can be rejected.");
        }
        this.status = EnrollmentStatus.REJECTED;
        this.decidedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    public void cancel(){
        if (this.status != EnrollmentStatus.PENDING){
            throw new IllegalStateException("Only pending enrollments can be cancelled.");
        }
        this.status = EnrollmentStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
    }
}
