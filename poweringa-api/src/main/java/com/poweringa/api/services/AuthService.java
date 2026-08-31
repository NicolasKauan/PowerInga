package com.poweringa.api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.poweringa.api.dtos.LoginRequestDTO;
import com.poweringa.api.dtos.RegisterRequestDTO;
import com.poweringa.api.exceptions.UserAlreadyExists;
import com.poweringa.api.models.User;
import com.poweringa.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public String login(LoginRequestDTO data) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                data.email(), data.senha()
        );

        Authentication auth = this.authenticationManager.authenticate(credentials);

        User user = (User) auth.getPrincipal();

        return tokenService.generateToken(user);
    }

    public void register(RegisterRequestDTO data) {
        System.out.println(data.email());

        if (userRepository.existsByEmail(data.email())) {
            throw new UserAlreadyExists();
        }

        String encryptedPassword = passwordEncoder.encode(data.senha());

        User newUser = new User(data.email(), encryptedPassword, data.cargo(), data.descricao());

        userRepository.save(newUser);
    }
}
