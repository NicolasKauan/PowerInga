package com.poweringa.api.services;

import com.poweringa.api.dtos.ProdutoRequestDTO;
import com.poweringa.api.dtos.ProdutoResponseDTO;
import com.poweringa.api.models.ProdutosModel;
import com.poweringa.api.repositories.ProdutosRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutosRepository produtosRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    void findAllDeveRetornarListaDeProdutos() {
        ProdutosModel model = new ProdutosModel("id-1", "Lâmpada LED", 50);
        when(produtosRepository.findAll()).thenReturn(List.of(model));

        List<ProdutoResponseDTO> resultado = produtoService.findAll();

        assertEquals(1, resultado.size());
        assertEquals("Lâmpada LED", resultado.get(0).descricao());
        verify(produtosRepository).findAll();
    }

    @Test
    void findByIdDeveRetornarProdutoQuandoEncontrado() {
        ProdutosModel model = new ProdutosModel("id-1", "Painel Solar", 500);
        when(produtosRepository.findById("id-1")).thenReturn(Optional.of(model));

        ProdutoResponseDTO resultado = produtoService.findById("id-1");

        assertEquals("Painel Solar", resultado.descricao());
        assertEquals(500, resultado.valorPontos());
        verify(produtosRepository).findById("id-1");
    }

    @Test
    void findByIdDeveLancarExcecaoQuandoNaoEncontrado() {
        when(produtosRepository.findById("id-invalido")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> produtoService.findById("id-invalido"));
        verify(produtosRepository).findById("id-invalido");
    }

    @Test
    void saveDeveSalvarERetornarProduto() {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Bicicleta", 1000);
        ProdutosModel modelSalvo = new ProdutosModel("id-2", "Bicicleta", 1000);

        when(produtosRepository.save(any(ProdutosModel.class))).thenReturn(modelSalvo);

        ProdutoResponseDTO resultado = produtoService.save(requestDTO);

        assertEquals("id-2", resultado.id());
        assertEquals("Bicicleta", resultado.descricao());
        verify(produtosRepository).save(any(ProdutosModel.class));
    }

    @Test
    void updateDeveAtualizarERetornarProduto() {
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Bicicleta Atualizada", 1200);
        ProdutosModel modelExistente = new ProdutosModel("id-2", "Bicicleta", 1000);
        ProdutosModel modelAtualizado = new ProdutosModel("id-2", "Bicicleta Atualizada", 1200);

        when(produtosRepository.findById("id-2")).thenReturn(Optional.of(modelExistente));
        when(produtosRepository.save(any(ProdutosModel.class))).thenReturn(modelAtualizado);

        ProdutoResponseDTO resultado = produtoService.update("id-2", requestDTO);

        assertEquals("Bicicleta Atualizada", resultado.descricao());
        assertEquals(1200, resultado.valorPontos());
        verify(produtosRepository).save(any(ProdutosModel.class));
    }

    @Test
    void deleteDeveExcluirProdutoQuandoExistir() {
        ProdutosModel modelExistente = new ProdutosModel("id-2", "Bicicleta", 1000);
        when(produtosRepository.findById("id-2")).thenReturn(Optional.of(modelExistente));

        produtoService.delete("id-2");

        verify(produtosRepository).deleteById("id-2");
    }
}