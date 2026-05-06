package com.chambaya.backend.reputation.infrastructure.persistence.repositories;

import com.chambaya.backend.reputation.domain.model.Review;
import com.chambaya.backend.reputation.domain.repositories.ReviewRepository;
import com.chambaya.backend.reputation.infrastructure.persistence.documents.ReviewDocument;
import com.chambaya.backend.reputation.infrastructure.persistence.mappers.ReviewMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoReviewRepository implements ReviewRepository {
    private final SpringDataReviewRepository springDataReviewRepository;

    public MongoReviewRepository(SpringDataReviewRepository springDataReviewRepository) {
        this.springDataReviewRepository = springDataReviewRepository;
    }

    @Override
    public Review save(Review review) {
        ReviewDocument document = ReviewMapper.toDocument(review);
        ReviewDocument savedDocument = springDataReviewRepository.save(document);
        return ReviewMapper.toDomain(savedDocument);
    }

    @Override
    public Optional<Review> findById(String id) {
        return springDataReviewRepository.findById(id)
                .map(ReviewMapper::toDomain);
    }

    @Override
    public List<Review> findAll() {
        return springDataReviewRepository.findAll()
                .stream()
                .map(ReviewMapper::toDomain)
                .toList();
    }

    @Override
    public List<Review> findByJobId(String jobId) {
        return springDataReviewRepository.findByJobId(jobId)
                .stream()
                .map(ReviewMapper::toDomain)
                .toList();
    }

    @Override
    public List<Review> findByReviewerId(String reviewerId) {
        return springDataReviewRepository.findByReviewerId(reviewerId)
                .stream()
                .map(ReviewMapper::toDomain)
                .toList();
    }

    @Override
    public List<Review> findByReviewedUserId(String reviewedUserId) {
        return springDataReviewRepository.findByReviewedUserId(reviewedUserId)
                .stream()
                .map(ReviewMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByJobIdAndReviewerIdAndReviewedUserId(
            String jobId,
            String reviewerId,
            String reviewedUserId
    ) {
        return springDataReviewRepository.existsByJobIdAndReviewerIdAndReviewedUserId(
                jobId,
                reviewerId,
                reviewedUserId
        );
    }

    @Override
    public void deleteById(String id){
        springDataReviewRepository.deleteById(id);
    }

}
