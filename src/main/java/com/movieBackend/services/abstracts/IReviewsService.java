package com.movieBackend.services.abstracts;


import com.movieBackend.dtos.DtoReviews;
import com.movieBackend.entities.Reviews;

import java.util.Date;
import java.util.List;


public interface IReviewsService {
    List<Reviews> getReviewsById(Integer id);
    DtoReviews createReview(DtoReviews dtoReviews);
    List<Reviews> getReviewByMovieId(Integer movieId,String sort);
    List<Reviews> getReviewsByUserId(Integer userId);
}
