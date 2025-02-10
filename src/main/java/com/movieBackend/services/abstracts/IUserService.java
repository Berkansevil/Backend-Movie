package com.movieBackend.services.abstracts;


import com.movieBackend.dtos.DtoUser;
import com.movieBackend.dtos.DtoUserIU;
import com.movieBackend.entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserService  {
    User saveUser(DtoUserIU dtoUserIU);

    List<DtoUser> getAllUser();

    ResponseEntity<DtoUser> getUserById(Integer id);

    String deleteUser(Integer id);

    DtoUser updateUser(Integer id,DtoUserIU dtoUpdateUser);

    DtoUserIU getCurrentUserProfile(String username);

    void updateUserProfile(String currentUsername,DtoUser dtoUser);

}
