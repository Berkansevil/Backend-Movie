package com.movieBackend.services.abstracts;

import com.movieBackend.dtos.MovieDto;
import com.movieBackend.dtos.MovieResponseDto;
import com.movieBackend.dtos.UpdateReviewDto;
import com.movieBackend.entities.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService {

    List<MovieResponseDto> getTrendingMovies();

    MovieResponseDto save(MovieDto movieDto);

    List<MovieResponseDto>  getMovieReleaseDate();

    List<MovieResponseDto> getMoviesByTitle(String title);

    List<MovieResponseDto> getAllMovies();

    List<MovieResponseDto> getTopRatedMovies();

    MovieDto getMovieById(Integer id);

    String commentDelete(int movieId, int reviewId);

    Movie rateMovie(int movieId, int rate);

    Page<Movie> searchMovies(Pageable pageable, String title);

    String deleteById(int id);

    Page<MovieResponseDto> getAllMoviesForAdmin(Pageable pageable);

     Movie updateReview(Integer movieId, Long reviewId, UpdateReviewDto updateReviewDto);

    void multipleSave(MultipartFile file) throws Exception;
    MovieDto approveMovie(Integer id);

    UpdateReviewDto submitReview(Integer id, UpdateReviewDto updateReview);
}
