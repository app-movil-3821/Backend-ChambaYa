package com.chambaya.backend.reputation.infrastructure.persistence.repositories;

import com.chambaya.backend.reputation.infrastructure.persistence.documents.ReviewDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpringDataReviewRepository extends MongoRepository<ReviewDocument, String> {

    List<ReviewDocument> findByJobId(String jobId);

    List<ReviewDocument> findByReviewerId(String reviewerId);

    List<ReviewDocument> findByReviewedUserId(String reviewedUserId);

    boolean existsByJobIdAndReviewerIdAndReviewedUserId(
            String jobId,
            String reviewerId,
            String reviewedUserId
    );

}
