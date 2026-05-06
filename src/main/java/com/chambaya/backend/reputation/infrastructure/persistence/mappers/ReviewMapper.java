package com.chambaya.backend.reputation.infrastructure.persistence.mappers;

import com.chambaya.backend.reputation.domain.model.Review;
import com.chambaya.backend.reputation.infrastructure.persistence.documents.ReviewDocument;

public class ReviewMapper {
    private ReviewMapper() {}
    public static ReviewDocument toDocument(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewDocument(
                review.getId(),
                review.getJobId(),
                review.getReviewerId(),
                review.getReviewedUserId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }

    public static Review toDomain(ReviewDocument document){
        if (document == null) {
            return null;
        }
        return new Review(
                document.getId(),
                document.getJobId(),
                document.getReviewerId(),
                document.getReviewedUserId(),
                document.getRating(),
                document.getComment(),
                document.getCreatedAt()

        );
    }
}
