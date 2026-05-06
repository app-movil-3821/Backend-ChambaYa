package com.chambaya.backend.reputation.interfaces.rest.controllers;

import com.chambaya.backend.reputation.application.services.ReviewApplicationService;
import com.chambaya.backend.reputation.domain.model.Review;
import com.chambaya.backend.reputation.interfaces.rest.assemblers.ReviewResourceAssembler;
import com.chambaya.backend.reputation.interfaces.rest.resources.CreateReviewResource;
import com.chambaya.backend.reputation.interfaces.rest.resources.RatingSummaryResource;
import com.chambaya.backend.reputation.interfaces.rest.resources.ReviewResource;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewsController {
    private final ReviewApplicationService reviewApplicationService;
    public ReviewsController(ReviewApplicationService reviewApplicationService){
        this.reviewApplicationService = reviewApplicationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResource createReview(@Valid @RequestBody CreateReviewResource resource){
        Review review = reviewApplicationService.createReview(
                ReviewResourceAssembler.toCreateReviewCommand(resource)
        );
        return ReviewResourceAssembler.toResource(review);
    }

    @GetMapping
    public List<ReviewResource> getAllReviews(){
        return reviewApplicationService.findAll()
                .stream()
                .map(ReviewResourceAssembler::toResource)
                .toList();
    }

    @GetMapping("/{id}")
    public ReviewResource getReviewById(@PathVariable String id){
        Review review = reviewApplicationService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        return ReviewResourceAssembler.toResource(review);
    }
    @GetMapping("/job/{jobId}")
    public List<ReviewResource> getReviewsByJobId(@PathVariable String jobId) {
        return reviewApplicationService.findByJobId(jobId)
                .stream()
                .map(ReviewResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/reviewer/{reviewerId}")
    public List<ReviewResource> getReviewsByReviewerId(@PathVariable String reviewerId) {
        return reviewApplicationService.findByReviewerId(reviewerId)
                .stream()
                .map(ReviewResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/user/{userId}")
    public List<ReviewResource> getReviewsByReviewedUserId(@PathVariable String userId) {
        return reviewApplicationService.findByReviewedUserId(userId)
                .stream()
                .map(ReviewResourceAssembler::toResource)
                .toList();
    }
    @GetMapping("/user/{userId}/summary")
    public RatingSummaryResource getRatingSummary(@PathVariable String userId) {
        return ReviewResourceAssembler.toResource(
                reviewApplicationService.getRatingSummary(userId)
        );
    }

}
