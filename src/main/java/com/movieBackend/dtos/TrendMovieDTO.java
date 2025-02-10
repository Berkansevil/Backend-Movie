package com.movieBackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrendMovieDTO {

    private Integer userId;

    private Integer movieId;

    private Timestamp addedDate;

}
