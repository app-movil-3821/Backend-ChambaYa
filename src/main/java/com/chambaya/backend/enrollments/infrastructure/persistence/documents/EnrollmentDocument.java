package com.chambaya.backend.enrollments.infrastructure.persistence.documents;

import com.chambaya.backend.enrollments.domain.model.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "enrollments")
public class EnrollmentDocument {
    @Id
    private String id;
    private String jobId;
    private String workerId;
    private String contractorId;
    private EnrollmentStatus status;
    private LocalDateTime appliedAt;
    private LocalDateTime decidedAt;
    private LocalDateTime updatedAt;
}
