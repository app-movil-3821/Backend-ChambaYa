package com.chambaya.backend.reputation.infrastructure.persistence.documents;

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
@Document(collection = "reviews")
public class ReviewDocument {
    @Id
    private String id;
    private String jobId;
    private String reviewerId;
    private String reviewedUserId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;
}
