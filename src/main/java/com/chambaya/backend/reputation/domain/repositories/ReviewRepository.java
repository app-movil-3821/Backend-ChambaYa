package com.chambaya.backend.reputation.domain.repositories;

import com.chambaya.backend.reputation.domain.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {
    Review save(Review review);
    Optional<Review> findById(String id);
    List<Review> findAll();
    List<Review> findByJobId(String jobId);
    List<Review> findByReviewerId(String reviewerId);
    List<Review> findByReviewedUserId(String reviewedUserId);
    boolean existsByJobIdAndReviewerIdAndReviewedUserId(
            String jobId,
            String reviewerId,
            String reviewedUserId
    );

    void deleteById(String id);
}
