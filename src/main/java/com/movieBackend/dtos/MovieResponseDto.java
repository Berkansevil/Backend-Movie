package com.movieBackend.dtos;

import com.movieBackend.entities.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponseDto {

    private String title;

    private String description;

    private String director;

    private String genre;

    private Date releaseDate;

    private List<Integer> favoritedByUser;

    private Double imdb;

    private List<TrendMovieDTO> trendMovies;

    public MovieResponseDto(Movie movie) {
        this.title = movie.getTitle();
        this.description = movie.getDescription();
        this.director = movie.getDirector();
        this.genre = movie.getGenre();
        this.releaseDate = movie.getReleaseDate();
        this.imdb = movie.getImdb();
    }

    private Movie.MovieStatus status;
}
