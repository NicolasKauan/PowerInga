package com.poweringa.api.security;

import com.poweringa.api.enums.UserRole;
import com.poweringa.api.models.User;
import com.poweringa.api.services.TokenService;
import com.poweringa.api.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private SecurityFilter securityFilter;

    @Test
    void recoverTokenDeveRetornarTokenSemBearer() {
        when(request.getHeader("Authorization")).thenReturn("Bearer meu-token-123");

        String token = securityFilter.recoverToken(request);

        assertEquals("meu-token-123", token);
    }

    @Test
    void recoverTokenDeveRetornarNullQuandoNaoHouverHeader() {
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = securityFilter.recoverToken(request);

        assertNull(token);
    }

    @Test
    void doFilterInternalDeveAutenticarQuandoTokenForValido() throws ServletException, IOException {
        User user = new User("teste@email.com", "senha", UserRole.CLIENTE, "Desc");

        // Limpa o contexto de segurança antes do teste
        SecurityContextHolder.clearContext();

        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido");
        when(tokenService.validateToken("token-valido")).thenReturn("teste@email.com");
        when(userService.loadUserByUsername("teste@email.com")).thenReturn(user);

        // Chama o filtro
        securityFilter.doFilterInternal(request, response, filterChain);

        // Verifica se o usuário foi autenticado no contexto do Spring
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("teste@email.com", SecurityContextHolder.getContext().getAuthentication().getName());

        // Verifica se a requisição continuou o fluxo
        verify(filterChain).doFilter(request, response);
    }
}