package com.movieBackend.services.concretes;

import com.movieBackend.dtos.MovieDto;
import com.movieBackend.dtos.MovieResponseDto;
import com.movieBackend.dtos.UpdateReviewDto;
import com.movieBackend.entities.Movie;
import com.movieBackend.entities.Reviews;
import com.movieBackend.entities.TrendMovie;
import com.movieBackend.exception.movie.MovieNotFoundException;
import com.movieBackend.repositories.MovieRepository;
import com.movieBackend.repositories.ReviewsRepository;
import com.movieBackend.repositories.TrendMovieRepository;
import com.movieBackend.repositories.UserRepository;
import com.movieBackend.services.abstracts.MovieService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final TrendMovieRepository trendMovieRepository;
    private final ReviewsRepository reviewsRepository;


    @Override
    public List<MovieResponseDto> getTrendingMovies() {

        List<TrendMovie> trendMovies = trendMovieRepository.findAll();

        return trendMovies.stream()
                .map(trendMovie -> {
                    MovieResponseDto dto = new MovieResponseDto();
                    BeanUtils.copyProperties(trendMovie.getMovie(), dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public MovieResponseDto save(MovieDto movieDto) {

        MovieResponseDto responseDto = new MovieResponseDto();
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieDto, movie);

        Movie responseMovie = movieRepository.save(movie);
        BeanUtils.copyProperties(responseMovie, responseDto);

        return responseDto;
    }

    @Override
    public List<MovieResponseDto> getMovieReleaseDate() {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_YEAR, -14);
        Date twoWeeksAgo = cal.getTime();

        List<Movie> movies = movieRepository.findByReleaseDateGreaterThan(twoWeeksAgo);
        return movies.stream().map(movie -> {
                    MovieResponseDto dto = new MovieResponseDto();
                    BeanUtils.copyProperties(movie, dto);
                    return dto;
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<MovieResponseDto> getMoviesByTitle(String title) {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .map(movie -> {
                    MovieResponseDto dto = new MovieResponseDto();
                    BeanUtils.copyProperties(movie, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieResponseDto> getAllMovies() {     //listing movies.
        List<Movie> allMovies = movieRepository.findAll();
        return allMovies.stream()
                .map(movie -> {
                    MovieResponseDto dto = new MovieResponseDto();
                    BeanUtils.copyProperties(movie, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieResponseDto> getTopRatedMovies() {

        try {
            List<Movie> movies = movieRepository.findTopRatedMovies();

            return movies.stream()
                    .map(movie -> {
                        MovieResponseDto dto = new MovieResponseDto();
                        BeanUtils.copyProperties(movie, dto);
                        return dto;
                    })
                    .collect(Collectors.toList());
        }   catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
    @Override
    public MovieDto getMovieById(Integer id){
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()){
            Movie movie = optionalMovie.get();
            MovieDto movieDto = new MovieDto();
            BeanUtils.copyProperties(movie, movieDto);
            return movieDto;
        }else {
            return null;
        }
    }

    @Transactional
    public Movie updateReview(Integer movieId, Long reviewId, UpdateReviewDto updateReviewDto){
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()-> new RuntimeException("Movie not found"));

        Reviews reviews = movie.getReviews().stream()
                .filter(r -> r.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Review not found"));

        reviews.setReview(updateReviewDto.getReview());
        reviews.setRating(updateReviewDto.getRating());
        movieRepository.save(movie);
        return movie;
    }

    @Override
    public String commentDelete(int movieId, int reviewId){
        Movie movie = this.movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found."));

        List<Reviews> reviews = movie.getReviews();
        Reviews review = reviews.stream().filter(reviews1 -> reviews1.getId() == reviewId).findFirst()
                .orElseThrow(() -> new RuntimeException("Not found movie review."));
        reviews.remove(review);
        movieRepository.save(movie);
        return "Review successfully deleted.";
    }

    @Override
    public Movie rateMovie(int movieId, int rate) {
        if (rate < 0 || rate > 10) {
            throw new IllegalArgumentException("Rate must be between 0 and 10.");
        }

        Movie movie = this.movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found."));

        if (movie.getImdb()==null){
            movie.setImdb(0.0);
            movieRepository.save(movie);
        }

        movie.setImdb(movie.getImdb() + rate);
        movieRepository.save(movie);
        return movie;
    }

    @Override
    public Page<Movie> searchMovies(Pageable pageable, String title){
        return movieRepository.findByTitleEqualsIgnoreCase(title, pageable);
    }

    @Override
    public Page<MovieResponseDto> getAllMoviesForAdmin(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(movie -> new MovieResponseDto(movie));
    }

    @Override
    public String deleteById(int id) {

        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movie not found");
        }

        movieRepository.deleteById(id);

        return "Movie has been deleted successfully.";

    }

    @Override
    public void multipleSave(MultipartFile file) throws Exception {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String[] csvRow;
            while ((csvRow = csvReader.readNext()) != null) {
                if (csvRow.length < 5) {
                    System.out.println("Eksik veri nedeniyle satır atlandı: " + Arrays.toString(csvRow));
                    continue;
                }
                String title = csvRow[0];
                String description = csvRow[1];
                String director = csvRow[2];
                String genre = csvRow[3];
                String releaseDateString = csvRow[4];

                Date releaseDate = null;
                try {
                    releaseDate = dateFormat.parse(releaseDateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Movie movie = new Movie();

                movie.setTitle(title);
                movie.setDescription(description);
                movie.setDirector(director);
                movie.setGenre(genre);
                movie.setReleaseDate(releaseDate);

                movieRepository.save(movie);
            }
        } catch (CsvException e) {
            e.printStackTrace();
        }

    }
    @Override
    public MovieDto approveMovie(Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found"));

        movie.setStatus(Movie.MovieStatus.APPROVED);
        Movie savedMovie = movieRepository.save(movie);
        MovieDto movieDto = new MovieDto();
        BeanUtils.copyProperties(savedMovie, movieDto);
        return movieDto;
    }

    @Override
    public UpdateReviewDto submitReview(Integer id, UpdateReviewDto updateReview) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Reviews review = new Reviews();
        BeanUtils.copyProperties(updateReview, review);
        review.setMovie(movie);

        Reviews savedReview = reviewsRepository.save(review);
        UpdateReviewDto updateDto = new UpdateReviewDto();
        BeanUtils.copyProperties(savedReview, updateDto);

        return updateDto;
    }

}
