package com.poweringa.api.controllers;

import com.poweringa.api.dtos.ProdutoRequestDTO;
import com.poweringa.api.dtos.ProdutoResponseDTO;
import com.poweringa.api.services.ProdutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutosControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutosController produtosController;

    @Test
    void saveDeveRetornarStatusCreatedEProdutoSalvo() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Mesa", 100);
        ProdutoResponseDTO responseMock = new ProdutoResponseDTO("1", "Mesa", 100);
        when(produtoService.save(request)).thenReturn(responseMock);

        ResponseEntity<ProdutoResponseDTO> response = produtosController.save(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseMock, response.getBody());
    }

    @Test
    void findAllDeveRetornarStatusOkEListaDeProdutos() {
        List<ProdutoResponseDTO> lista = List.of(new ProdutoResponseDTO("1", "Mesa", 100));
        when(produtoService.findAll()).thenReturn(lista);

        ResponseEntity<List<ProdutoResponseDTO>> response = produtosController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lista, response.getBody());
    }

    @Test
    void findByIdDeveRetornarStatusOkEProduto() {
        ProdutoResponseDTO responseMock = new ProdutoResponseDTO("1", "Mesa", 100);
        when(produtoService.findById("1")).thenReturn(responseMock);

        ResponseEntity<ProdutoResponseDTO> response = produtosController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMock, response.getBody());
    }

    @Test
    void deleteDeveRetornarStatusNoContent() {
        ResponseEntity<ProdutoResponseDTO> response = produtosController.delete("1");

        verify(produtoService).delete("1");
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void updateDeveRetornarStatusOkQuandoSucesso() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Mesa Nova", 150);
        ProdutoResponseDTO responseMock = new ProdutoResponseDTO("1", "Mesa Nova", 150);
        when(produtoService.update("1", request)).thenReturn(responseMock);

        ResponseEntity<ProdutoResponseDTO> response = produtosController.update("1", request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseMock, response.getBody());
    }

    @Test
    void updateDeveRetornarStatusBadRequestQuandoDerErro() {
        ProdutoRequestDTO request = new ProdutoRequestDTO("Mesa Nova", 150);
        // Simulando que o Service estourou um erro (id não encontrado, por exemplo)
        when(produtoService.update("1", request)).thenThrow(new RuntimeException("Erro"));

        // O Controller tem um bloco try/catch para esse método, o catch vai devolver 400 Bad Request
        ResponseEntity<ProdutoResponseDTO> response = produtosController.update("1", request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}