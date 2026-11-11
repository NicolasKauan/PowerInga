package com.poweringa.api.controllers;

import com.poweringa.api.dtos.UserResponseDTO;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.models.User;
import com.poweringa.api.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void findLoggedUserDeveRetornarStatusOkEOsDadosDoUsuarioLogado() {
        User usuarioLogado = new User("cliente@email.com", "123456", UserRole.CLIENTE, "Descrição");
        usuarioLogado.setId("cliente-1");
        usuarioLogado.setPontos(45);
        UserResponseDTO dto = new UserResponseDTO(
                "cliente-1", "cliente@email.com", "Descrição", UserRole.CLIENTE, 45
        );
        when(userService.toDTO(usuarioLogado)).thenReturn(dto);

        ResponseEntity<UserResponseDTO> response = userController.findLoggedUser(usuarioLogado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }
}
