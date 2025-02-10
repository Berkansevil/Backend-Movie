package com.movieBackend.repositories;

import com.movieBackend.entities.TrendMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendMovieRepository extends JpaRepository<TrendMovie, Integer> {
}
