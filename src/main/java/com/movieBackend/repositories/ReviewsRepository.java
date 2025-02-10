package com.movieBackend.repositories;


import com.movieBackend.entities.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewsRepository extends JpaRepository<Reviews, Integer> {
    List<Reviews> findByMovieId(Integer movieId);
    List<Reviews> findByUserId(Integer userId);
}
