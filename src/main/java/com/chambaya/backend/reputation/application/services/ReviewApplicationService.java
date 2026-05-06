package com.chambaya.backend.reputation.application.services;

import com.chambaya.backend.reputation.application.commands.CreateReviewCommand;
import com.chambaya.backend.reputation.domain.model.RatingSummary;
import com.chambaya.backend.reputation.domain.model.Review;
import com.chambaya.backend.reputation.domain.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Service
public class ReviewApplicationService {
    private final ReviewRepository reviewRepository;
    public ReviewApplicationService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(CreateReviewCommand command){
        if (reviewRepository.existsByJobIdAndReviewerIdAndReviewedUserId(
                command.jobId(),
                command.reviewerId(),
                command.reviewedUserId()
        )){
            throw new IllegalArgumentException("Review already exists for this job and user");
        }
        Review review = new Review(
                null,
                command.jobId(),
                command.reviewerId(),
                command.reviewedUserId(),
                command.rating(),
                command.comment(),
                LocalDateTime.now()
        );
        review.validateRating();
        return reviewRepository.save(review);
    }
    public Optional<Review> findById(String id){
        return reviewRepository.findById(id);
    }
    public List<Review> findAll(){
        return reviewRepository.findAll();
    }
    public List<Review> findByJobId(String jobId) {
        return reviewRepository.findByJobId(jobId);
    }
    public List<Review> findByReviewerId(String reviewerId) {
        return reviewRepository.findByReviewerId(reviewerId);
    }
    public List<Review> findByReviewedUserId(String reviewedUserId) {
        return reviewRepository.findByReviewedUserId(reviewedUserId);
    }
    public RatingSummary getRatingSummary(String userId) {
        List<Review> reviews = reviewRepository.findByReviewedUserId(userId);
        if (reviews.isEmpty()) {
            return new RatingSummary(userId, 0.0, 0);
        }
        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        return new RatingSummary(userId, average, reviews.size());
    }


}
