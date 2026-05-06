package com.chambaya.backend.jobs.infrastructure.persistence.documents;

import com.chambaya.backend.jobs.domain.model.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class JobDocument {

    @Id
    private String id;
    private String contractorId;
    private String title;
    private String description;
    private String category;
    private List<String> requiredSkills;
    private BigDecimal paymentAmount;
    private LocationDocument location;
    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private JobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
