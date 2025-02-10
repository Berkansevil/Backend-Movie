package com.movieBackend.controllers;

import com.movieBackend.dtos.ActorOrDirectorDTO;
import com.movieBackend.services.concretes.ActorOrDirectorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping(value = "/api/actor-director")
public class ActorOrDirectorController {
    private  final ActorOrDirectorService service;

    @PostMapping(value = "/save",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody ActorOrDirectorDTO directorDTO ){
        ActorOrDirectorDTO DTO = service.save(directorDTO);
        return new ResponseEntity<>(DTO, HttpStatus.CREATED);
    }



}
