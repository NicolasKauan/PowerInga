package com.poweringa.api.controllers;

import com.poweringa.api.dtos.SolicitacaoCreateDTO;
import com.poweringa.api.dtos.SolicitacaoResponseDTO;
import com.poweringa.api.enums.CategoriaRecompensa;
import com.poweringa.api.enums.SolicitacaoStatus;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.models.User;
import com.poweringa.api.services.SolicitacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitacaoControllerTest {

    @Mock
    private SolicitacaoService solicitacaoService;

    @InjectMocks
    private SolicitacaoController solicitacaoController;

    private User criarUsuario(String id, UserRole cargo) {
        User usuario = new User("usuario@email.com", "123456", cargo, "Descrição");
        usuario.setId(id);
        return usuario;
    }

    @Test
    void findAllDeveRetornarStatusOkEListaDeSolicitacoes() {
        User usuarioLogado = criarUsuario("cliente-1", UserRole.CLIENTE);
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO(
                "sol-1", "Descrição", CategoriaRecompensa.MICRO_ACAO, SolicitacaoStatus.PENDENTE, "cliente-1"
        );
        when(solicitacaoService.findAll(usuarioLogado)).thenReturn(List.of(dto));

        ResponseEntity<List<SolicitacaoResponseDTO>> response = solicitacaoController.findAll(usuarioLogado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void findByIdDeveRetornarStatusOkEASolicitacao() {
        User usuarioLogado = criarUsuario("cliente-1", UserRole.CLIENTE);
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO(
                "sol-1", "Descrição", CategoriaRecompensa.MICRO_ACAO, SolicitacaoStatus.PENDENTE, "cliente-1"
        );
        when(solicitacaoService.findById("sol-1", usuarioLogado)).thenReturn(dto);

        ResponseEntity<SolicitacaoResponseDTO> response = solicitacaoController.findById("sol-1", usuarioLogado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void createDeveRetornarStatusOkEASolicitacaoCriada() {
        User usuarioLogado = criarUsuario("cliente-1", UserRole.CLIENTE);
        SolicitacaoCreateDTO request = new SolicitacaoCreateDTO("Plantei árvores", CategoriaRecompensa.ACAO_LOCALIZADA);
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO(
                "sol-2", "Plantei árvores", CategoriaRecompensa.ACAO_LOCALIZADA, SolicitacaoStatus.PENDENTE, "cliente-1"
        );
        when(solicitacaoService.create(request, usuarioLogado)).thenReturn(dto);

        ResponseEntity<SolicitacaoResponseDTO> response = solicitacaoController.create(request, usuarioLogado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void approveDeveRetornarStatusOkEASolicitacaoAprovada() {
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO(
                "sol-1", "Descrição", CategoriaRecompensa.MICRO_ACAO, SolicitacaoStatus.APROVADO, "cliente-1"
        );
        when(solicitacaoService.approve("sol-1")).thenReturn(dto);

        ResponseEntity<SolicitacaoResponseDTO> response = solicitacaoController.approve("sol-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SolicitacaoStatus.APROVADO, response.getBody().status());
    }

    @Test
    void rejectDeveRetornarStatusOkEASolicitacaoReprovada() {
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO(
                "sol-1", "Descrição", CategoriaRecompensa.MICRO_ACAO, SolicitacaoStatus.REPROVADO, "cliente-1"
        );
        when(solicitacaoService.reject("sol-1")).thenReturn(dto);

        ResponseEntity<SolicitacaoResponseDTO> response = solicitacaoController.reject("sol-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SolicitacaoStatus.REPROVADO, response.getBody().status());
    }
}