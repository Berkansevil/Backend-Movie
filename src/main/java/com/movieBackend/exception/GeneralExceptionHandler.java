package com.movieBackend.exception;

import com.movieBackend.exception.movie.MovieNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GeneralExceptionHandler {


    @ExceptionHandler(value = MovieNotFoundException.class)
    public ResponseEntity<ProplemDetails> handleMovieNotFoundException(MovieNotFoundException e, WebRequest request) {
        ProplemDetails proplemDetails = new ProplemDetails();
        proplemDetails.setPath(request.getDescription(false));
        proplemDetails.setMessage(e.getMessage());
        proplemDetails.setErrorCode(HttpStatus.NOT_FOUND);
        proplemDetails.setLocalDate(LocalDateTime.now());
        return new ResponseEntity<>(proplemDetails, HttpStatus.NOT_FOUND);
    }
}
