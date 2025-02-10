package com.movieBackend.services.concretes;

import com.movieBackend.dtos.auth.AuthenticationResponse;
import com.movieBackend.dtos.auth.LoginRequest;
import com.movieBackend.dtos.auth.RegisterRequest;
import com.movieBackend.entities.Role;
import com.movieBackend.entities.User;
import com.movieBackend.entities.UserRole;
import com.movieBackend.jwt.JwtService;
import com.movieBackend.repositories.RoleRepository;
import com.movieBackend.repositories.UserRepository;
import com.movieBackend.services.abstracts.IAuthService;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final AuthenticationProvider authenticationProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final JavaMailSender emailSender;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        initializeRoles();

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();

        user.setUserRoles(new ArrayList<>());

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("USER role is not found."));

        UserRole userRoleAssignment = new UserRole();
        userRoleAssignment.setUser(user);
        userRoleAssignment.setRole(userRole);

        user.getUserRoles().add(userRoleAssignment);

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);

    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
            authenticationProvider.authenticate(auth);
            Optional<User> optional = userRepository.findByUsername(request.getUsername());
            String token = jwtService.generateToken(optional.get());
            return new AuthenticationResponse(token);
        } catch (Exception e) {
            System.out.println("Username or password is incorrect");
        }

        return null;
    }

    private void initializeRoles() {
        if (roleRepository.findByRoleName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setRoleName("USER");
            roleRepository.save(userRole);
        }
    }

    public void requestPasswordReset(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String token = jwtService.generateToken(user);
            String resetLink = "http://movie.com/reset-password?token=" + token;
            sendPasswordResetEmail(user.getEmail(), resetLink);
        } else {
            throw new IllegalArgumentException("User  not found");
        }
    }

    private void sendPasswordResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, please click the link below:\n\n" + resetLink);
        emailSender.send(message);
    }


}


