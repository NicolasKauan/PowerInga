package com.poweringa.api.controllers;

import com.poweringa.api.dtos.LoginRequestDTO;
import com.poweringa.api.dtos.LoginResponseDTO;
import com.poweringa.api.dtos.RegisterRequestDTO;
import com.poweringa.api.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO data) {
        String token = authService.login(data);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO data) {
        authService.register(data);

        return ResponseEntity.ok("Usuário criado com sucesso!");
    }
}
