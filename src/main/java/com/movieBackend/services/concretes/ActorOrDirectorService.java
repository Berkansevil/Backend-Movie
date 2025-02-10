package com.movieBackend.services.concretes;

import com.movieBackend.dtos.ActorOrDirectorDTO;
import com.movieBackend.entities.ActorOrDirector;
import com.movieBackend.repositories.ActorOrDirectorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ActorOrDirectorService {
    private final ActorOrDirectorRepository actorOrDirectorRepository;

    public ActorOrDirectorDTO save(ActorOrDirectorDTO ActorDTO) {

        ActorOrDirector actorOrDirector = new ActorOrDirector();
        actorOrDirector.setName(ActorDTO.getName());
        actorOrDirector.setRole(ActorDTO.getRole());
        actorOrDirectorRepository.save(actorOrDirector);

        ActorDTO.setName(actorOrDirector.getName());
        ActorDTO.setRole(actorOrDirector.getRole());
        return ActorDTO;
    }


    public ActorOrDirectorDTO getById(Long actorID) {
        ActorOrDirector actorOrDirector = actorOrDirectorRepository.findById(actorID)
                .orElseThrow(() -> new RuntimeException("Actor or director not found."));

        ActorOrDirectorDTO directorDTO = new ActorOrDirectorDTO();
        directorDTO.setName(actorOrDirector.getName());
        directorDTO.setRole(actorOrDirector.getRole());
        return directorDTO;
    }


}
