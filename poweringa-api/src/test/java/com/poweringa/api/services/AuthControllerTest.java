package com.poweringa.api.controllers;

import com.poweringa.api.dtos.LoginRequestDTO;
import com.poweringa.api.dtos.LoginResponseDTO;
import com.poweringa.api.dtos.RegisterRequestDTO;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.services.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginDeveRetornarTokenEStatusOk() {
        LoginRequestDTO request = new LoginRequestDTO("teste@teste.com", "123");
        when(authService.login(request)).thenReturn("meu-token-jwt-falso");

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("meu-token-jwt-falso", response.getBody().token());
    }

    @Test
    void registerDeveRetornarMensagemEStatusOk() {
        RegisterRequestDTO request = new RegisterRequestDTO("teste@teste.com", "123", "Desc", UserRole.CLIENTE);

        ResponseEntity<String> response = authController.register(request);

        verify(authService).register(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário criado com sucesso!", response.getBody());
    }
}