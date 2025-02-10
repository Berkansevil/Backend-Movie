package com.movieBackend.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActorOrDirectorDTO {

    @NotNull(message = "Error: Name cannot be null.")
    private String name;

    @NotNull(message = "Error: Role cannot be null.")
    private String role;

}
