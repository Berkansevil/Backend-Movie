package com.movieBackend.controllers;

import com.movieBackend.dtos.DtoReviews;
import com.movieBackend.dtos.MovieDto;
import com.movieBackend.dtos.MovieResponseDto;
import com.movieBackend.dtos.UpdateReviewDto;
import com.movieBackend.entities.Movie;
import com.movieBackend.entities.Reviews;
import com.movieBackend.services.abstracts.IReviewsService;
import com.movieBackend.services.abstracts.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final IReviewsService reviewsService;



    @GetMapping("/trending")
    public ResponseEntity<List<MovieResponseDto>> getTrendingMovies() {
        List<MovieResponseDto> trendingMovies = movieService.getTrendingMovies();
        return ResponseEntity.ok(trendingMovies);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MovieResponseDto>> getMoviesByTitle(@RequestParam(name = "title") String title) {
        List<MovieResponseDto> movieResponse = movieService.getMoviesByTitle(title);

        if (movieResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movieResponse);
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieResponseDto> save(@RequestBody MovieDto movieDto) {
        MovieResponseDto movieResponseDto = movieService.save(movieDto);
        return ResponseEntity.ok(movieResponseDto);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<MovieResponseDto>> getMovieReleaseDate() {
        List<MovieResponseDto> movieResponseDto = movieService.getMovieReleaseDate();
        if (!movieResponseDto.isEmpty()) {
            return ResponseEntity.ok(movieResponseDto);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieResponseDto>> getTopRatedMovies(Pageable pageable) {

        try {
            List<MovieResponseDto> topRatedMovies = movieService.getTopRatedMovies();
            return ResponseEntity.ok(topRatedMovies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<DtoReviews>> getReviewsById(@PathVariable Integer id) {
        List<Reviews> allReviews = reviewsService.getReviewsById(id);
        List<DtoReviews> dtoReviewsList = allReviews.stream()
                .map(review -> DtoReviews.builder()
                        .review(review.getReview())
                        .rating(review.getRating())
                        .createdAt(review.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtoReviewsList, HttpStatus.OK);
    }

    @GetMapping("{id}/rating")
    public ResponseEntity<Double> getAverageRatingById(@PathVariable Integer id) {
        ResponseEntity<List<DtoReviews>> reviewsById = getReviewsById(id);

        if (reviewsById.getBody() == null || reviewsById.getBody().isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Integer> ratings = reviewsById.getBody().stream()
                .map(DtoReviews::getRating)
                .collect(Collectors.toList());

        double averageRating = ratings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        return ResponseEntity.ok(averageRating);
    }

    @GetMapping()
    public ResponseEntity<List<MovieResponseDto>> getAllMovies() {
        List<MovieResponseDto> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Integer id) {  //for the usage of this api you need the enter an integer as id not an string (localhost:8080/movies/1)
        MovieDto movie = movieService.getMovieById(id);
        if (movie != null) {
            return ResponseEntity.ok(movie);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with Id of:" + id + "is not found!!!");
        }
    }
    @DeleteMapping(value = "/{movieID}/{reviewId}")
    public ResponseEntity<String> movieOrreviewDelete(@PathVariable int movieID, @PathVariable int reviewId) {
        String s = movieService.commentDelete(movieID, reviewId);
        return ResponseEntity.ok(s);
    }
    @PostMapping(value = "/points/{movieId}/{rate}")
    public ResponseEntity<?> movieForRate(@PathVariable Integer movieId, @PathVariable Integer rate) {
        Movie movie = movieService.rateMovie(movieId, rate);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping("/pages")
    public Page<Movie> searchMovies(@RequestParam String title,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return movieService.searchMovies(pageable, title);
    }

    @GetMapping("/{movieId}/reviews/sorted")
    public List<Reviews> getReviewByMovieId(@PathVariable Integer movieId, @RequestParam String sort) {
        return reviewsService.getReviewByMovieId(movieId, sort);
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteById(@PathVariable int id) {
        movieService.deleteById(id);
        return ResponseEntity.ok("Movie has been deleted successfully.");
    }

    @GetMapping("/admin/movies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MovieResponseDto>> getAllMoviesForAdmin(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort sort= Sort.by(Sort.Direction.ASC, "title");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<MovieResponseDto> moviePage = movieService.getAllMoviesForAdmin(pageable);

        return ResponseEntity.ok(moviePage);
    }

    @PutMapping("/{id]/reviews/{review_id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer movieID, @PathVariable Long reviewID,
                                          @RequestBody UpdateReviewDto updateReviewDto) {
        Movie movie = movieService.updateReview(movieID, reviewID, updateReviewDto);
        return ResponseEntity.ok(movie);
    }

    @PostMapping("/admin/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> multipleSave(@RequestParam("file") MultipartFile file) {

        try {
            movieService.multipleSave(file);
            return ResponseEntity.ok("Movies uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading movies: " + e.getMessage());
        }

    }
    @PutMapping("admin/movies/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDto> approveMovie(@PathVariable Integer id) {
        MovieDto approvedMovie = movieService.approveMovie(id);
        return ResponseEntity.ok(approvedMovie);
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<UpdateReviewDto> submitReview(@PathVariable Integer id, @RequestBody UpdateReviewDto updateReview) {
        UpdateReviewDto updateReviewDto = movieService.submitReview(id, updateReview);
        return ResponseEntity.ok(updateReviewDto);
    }

}
