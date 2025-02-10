package com.movieBackend.services.abstracts;

import com.movieBackend.dtos.auth.AuthenticationResponse;
import com.movieBackend.dtos.auth.LoginRequest;
import com.movieBackend.dtos.auth.RegisterRequest;


public interface IAuthService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse login(LoginRequest request);

    public void requestPasswordReset(String email);

}
