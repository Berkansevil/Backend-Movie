package com.movieBackend.controllers;


import com.movieBackend.dtos.DtoReviews;
import com.movieBackend.entities.Reviews;
import com.movieBackend.services.abstracts.IReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsRestController {
    private final IReviewsService reviewsService;


    @PostMapping("/save")
    public ResponseEntity<DtoReviews> createReview(@RequestBody DtoReviews dtoReviews) {
        DtoReviews createdReview = reviewsService.createReview(dtoReviews);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

}
