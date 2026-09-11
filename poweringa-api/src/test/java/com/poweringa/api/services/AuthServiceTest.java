package com.poweringa.api.services;

import com.poweringa.api.dtos.LoginRequestDTO;
import com.poweringa.api.dtos.RegisterRequestDTO;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.exceptions.UserAlreadyExists;
import com.poweringa.api.models.User;
import com.poweringa.api.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginDeveRetornarTokenQuandoCredenciaisForemValidas() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("teste@email.com", "senha123");
        User usuarioFake = new User("teste@email.com", "senhaCriptografada", UserRole.CLIENTE, "Usuario Teste");

        // Simulando a classe Authentication do Spring Security
        Authentication authMock = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authMock);
        when(authMock.getPrincipal()).thenReturn(usuarioFake);
        when(tokenService.generateToken(usuarioFake)).thenReturn("token-jwt-falso");

        String token = authService.login(loginRequest);

        assertEquals("token-jwt-falso", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(usuarioFake);
    }

    @Test
    void registerDeveCriarUsuarioQuandoEmailNaoExistir() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO("novo@email.com", "senha123", "Novo User", UserRole.CLIENTE);

        when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");

        authService.register(registerRequest);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerDeveLancarExcecaoQuandoEmailJaExistir() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO("existente@email.com", "senha123", "User Existente", UserRole.CLIENTE);

        when(userRepository.existsByEmail("existente@email.com")).thenReturn(true);

        assertThrows(UserAlreadyExists.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }
}