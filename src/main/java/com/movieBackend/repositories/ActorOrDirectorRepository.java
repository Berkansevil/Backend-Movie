package com.movieBackend.repositories;

import com.movieBackend.entities.ActorOrDirector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorOrDirectorRepository extends JpaRepository<ActorOrDirector, Long> {
}
