package com.poweringa.api.services;

import com.poweringa.api.enums.UserRole;
import com.poweringa.api.models.User;
import com.poweringa.api.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findAllDeveRetornarListaDeUsuarios() {
        User user = new User("teste@email.com", "senha", UserRole.CLIENTE, "Desc");
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> resultado = userService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("teste@email.com", resultado.get(0).getEmail());
    }

    @Test
    void loadUserByUsernameDeveRetornarUsuarioQuandoExistir() {
        User user = new User("teste@email.com", "senha", UserRole.CLIENTE, "Desc");
        when(userRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(user));

        UserDetails resultado = userService.loadUserByUsername("teste@email.com");

        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getUsername());
    }

    @Test
    void loadUserByUsernameDeveLancarExcecaoQuandoNaoExistir() {
        when(userRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("inexistente@email.com"));
    }
}