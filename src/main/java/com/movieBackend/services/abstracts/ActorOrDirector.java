package com.movieBackend.services.abstracts;

import com.movieBackend.dtos.ActorOrDirectorDTO;

public interface ActorOrDirector {

        ActorOrDirectorDTO save(ActorOrDirectorDTO ActorDTO);
        ActorOrDirectorDTO getById(Long actorID);

    }
