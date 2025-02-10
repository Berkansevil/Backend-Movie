package com.movieBackend.controllers;

import ch.qos.logback.classic.Logger;
import com.movieBackend.dtos.DtoUser;
import com.movieBackend.dtos.DtoUserIU;
import com.movieBackend.dtos.RequestPasswordDto;
import com.movieBackend.dtos.auth.AuthenticationResponse;
import com.movieBackend.dtos.auth.LoginRequest;
import com.movieBackend.dtos.auth.RegisterRequest;
import com.movieBackend.services.abstracts.IAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during registration: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthenticationResponse response = authService.login(request);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Username or password is incorrect");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }
    @PostMapping("/password/reset")
    public ResponseEntity<String> requestPasswordReset(@RequestBody RequestPasswordDto requestPasswordDto) {
        try {
            authService.requestPasswordReset(requestPasswordDto.getEmail());
            return ResponseEntity.ok("Password reset link has been sent to your email.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}

