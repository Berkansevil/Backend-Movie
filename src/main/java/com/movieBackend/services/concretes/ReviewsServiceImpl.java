package com.movieBackend.services.concretes;

import com.movieBackend.dtos.DtoReviews;
import com.movieBackend.entities.Reviews;
import com.movieBackend.repositories.ReviewsRepository;
import com.movieBackend.services.abstracts.IReviewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl implements IReviewsService {
    private final ReviewsRepository reviewsRepository;

    @Override
    public List<Reviews> getReviewsById(Integer movieId) {
        return reviewsRepository.findByMovieId(movieId);
    }

    @Override
    public DtoReviews createReview(DtoReviews dtoReview) {
        Reviews review = new Reviews();
        BeanUtils.copyProperties(dtoReview, review);
        reviewsRepository.save(review);
        return dtoReview;
    }

    @Override
    public List<Reviews> getReviewsByUserId(Integer userId) {
        return reviewsRepository.findByUserId(userId);
    }

    @Override
    public List<Reviews> getReviewByMovieId(Integer movieId, String sort) {
        List<Reviews> reviews = reviewsRepository.findByMovieId(movieId);
        if ("date".equalsIgnoreCase(sort)) {
            reviews.sort(Comparator.comparing(Reviews::getCreatedAt));
        } else if ("rating".equalsIgnoreCase(sort)) {
            reviews.sort(Comparator.comparing(Reviews::getRating));
        }
        return reviews;
    }

}
