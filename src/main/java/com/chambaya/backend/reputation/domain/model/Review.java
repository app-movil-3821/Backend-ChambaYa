package com.chambaya.backend.reputation.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private String id;
    private String jobId;
    private String reviewerId;
    private String ReviewedUserId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public void validateRating(){
        if (rating < 1 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }


}
