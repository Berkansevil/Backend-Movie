package com.movieBackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoUserIU {

    private String username;

    private String email;

    private String password;

    private Date birtOfDate;
}
