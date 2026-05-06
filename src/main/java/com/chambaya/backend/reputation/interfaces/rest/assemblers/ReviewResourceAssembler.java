package com.chambaya.backend.reputation.interfaces.rest.assemblers;

import com.chambaya.backend.reputation.application.commands.CreateReviewCommand;
import com.chambaya.backend.reputation.domain.model.RatingSummary;
import com.chambaya.backend.reputation.domain.model.Review;
import com.chambaya.backend.reputation.interfaces.rest.resources.CreateReviewResource;
import com.chambaya.backend.reputation.interfaces.rest.resources.RatingSummaryResource;
import com.chambaya.backend.reputation.interfaces.rest.resources.ReviewResource;

public class ReviewResourceAssembler {
    private ReviewResourceAssembler() {}
    public static CreateReviewCommand toCreateReviewCommand(CreateReviewResource resource) {
        return new CreateReviewCommand(
                resource.jobId(),
                resource.reviewerId(),
                resource.reviewedUserId(),
                resource.rating(),
                resource.comment()
        );
    }

    public static ReviewResource toResource(Review review){
        return new ReviewResource(
                review.getId(),
                review.getJobId(),
                review.getReviewerId(),
                review.getReviewedUserId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
    public static RatingSummaryResource toResource(RatingSummary summary) {
        return new RatingSummaryResource(
                summary.getUserId(),
                summary.getAverageRating(),
                summary.getTotalReviews()
        );
    }
}
