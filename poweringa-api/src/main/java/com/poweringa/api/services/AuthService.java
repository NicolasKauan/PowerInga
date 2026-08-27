package com.poweringa.api.services;

import com.poweringa.api.dtos.LoginRequestDTO;
import com.poweringa.api.dtos.RegisterRequestDTO;
import com.poweringa.api.exceptions.UserAlreadyExists;
import com.poweringa.api.models.User;
import com.poweringa.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void login(LoginRequestDTO data) {
        UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                data.email(), data.senha()
        );

        Authentication auth = this.authenticationManager.authenticate(credentials);
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
