package com.movieBackend.controllers;


import com.movieBackend.dtos.DtoUser;
import com.movieBackend.dtos.DtoUserIU;
import com.movieBackend.entities.Reviews;
import com.movieBackend.entities.User;
import com.movieBackend.services.abstracts.IUserService;
import com.movieBackend.services.concretes.ReviewsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@EnableMethodSecurity
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/movie/user")
public class UserRestController {
    private final IUserService userService;
    private final ReviewsServiceImpl reviewsService;



    @PostMapping(path = "/save")
    public ResponseEntity<User> saveUser(@RequestBody DtoUserIU dtoUserIU) {
        try {
            User savedUser = userService.saveUser(dtoUserIU);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<DtoUser>> getAllUser() {
        try {
            List<DtoUser> users = userService.getAllUser();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        ResponseEntity<DtoUser> response = userService.getUserById(id);
        return response;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("Student deleted successfully", HttpStatus.OK);
    }

    @PutMapping(path = "/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable(name = "id") Integer id, @RequestBody DtoUserIU updatedUser) {

        DtoUser user = userService.updateUser(id, updatedUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }
    }

//    @PostMapping(path = "/register")
//    public ResponseEntity<DtoUser> registerUser(@RequestBody DtoUserIU dtoUserIU) {
//        try {
//            DtoUser savedUser = userService.saveUser(dtoUserIU);
//            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/profile")
    public ResponseEntity<DtoUserIU> getUserProfile(@RequestParam String userName) {

        try {
            DtoUserIU user = userService.getCurrentUserProfile(userName);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @RequestBody DtoUser dtoUser,
            @AuthenticationPrincipal UserDetails currentUser) {
        userService.updateUserProfile(currentUser.getUsername(), dtoUser);
        return ResponseEntity.ok("profile successfully updated");
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<Reviews>> getUserReviews(@PathVariable("id") Integer userId) {
        List<Reviews> reviews = reviewsService.getReviewsByUserId(userId);

        if (reviews.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(reviews);
    }

}
