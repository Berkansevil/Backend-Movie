package com.movieBackend.repositories;

import com.movieBackend.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByReleaseDateGreaterThan(Date releaseDate);

    @Query("SELECT m FROM Movie m JOIN m.reviews r GROUP BY m.id ORDER BY AVG(r.rating) DESC LIMIT 10")
    List<Movie> findTopRatedMovies();
    Page<Movie> findByTitleEqualsIgnoreCase(String title, Pageable pageable);
}
