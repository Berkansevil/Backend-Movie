package com.movieBackend.dtos;

import com.movieBackend.entities.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {

    private String title;

    private String description;

    private String director;

    private String genre;

    private Date releaseDate;

    private List<Integer> favoritedByUser;

    private Movie.MovieStatus status;

}
