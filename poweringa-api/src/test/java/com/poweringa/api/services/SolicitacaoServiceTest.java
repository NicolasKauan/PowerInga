package com.poweringa.api.services;

import com.poweringa.api.dtos.SolicitacaoCreateDTO;
import com.poweringa.api.dtos.SolicitacaoResponseDTO;
import com.poweringa.api.enums.CategoriaRecompensa;
import com.poweringa.api.enums.SolicitacaoStatus;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.exceptions.ResourceNotFound;
import com.poweringa.api.models.Solicitacao;
import com.poweringa.api.models.User;
import com.poweringa.api.repositories.SolicitacaoRepository;
import com.poweringa.api.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoServiceTest {

    @Mock
    private SolicitacaoRepository solicitacaoRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SolicitacaoService solicitacaoService;

    private User criarUsuario(String id, UserRole cargo, int pontos) {
        User usuario = new User("usuario@email.com", "123456", cargo, "Descrição");
        usuario.setId(id);
        usuario.setPontos(pontos);
        return usuario;
    }


    @Test
    void findAllQuandoGestorDeveRetornarTodasAsSolicitacoes() {
        User gestor = criarUsuario("gestor-1", UserRole.GESTOR, 0);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, gestor);
        solicitacao.setId("sol-1");

        when(solicitacaoRepository.findAll()).thenReturn(List.of(solicitacao));

        List<SolicitacaoResponseDTO> resultado = solicitacaoService.findAll(gestor);

        assertEquals(1, resultado.size());
        verify(solicitacaoRepository).findAll();
        verify(solicitacaoRepository, never()).findByUsuario_Id(any());
    }

    @Test
    void findAllQuandoClienteDeveRetornarApenasAsProprias() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 0);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, cliente);
        solicitacao.setId("sol-1");

        when(solicitacaoRepository.findByUsuario_Id("cliente-1")).thenReturn(List.of(solicitacao));

        List<SolicitacaoResponseDTO> resultado = solicitacaoService.findAll(cliente);

        assertEquals(1, resultado.size());
        verify(solicitacaoRepository).findByUsuario_Id("cliente-1");
        verify(solicitacaoRepository, never()).findAll();
    }


    @Test
    void findByIdQuandoGestorDeveRetornarSolicitacaoDeQualquerUsuario() {
        User gestor = criarUsuario("gestor-1", UserRole.GESTOR, 0);
        User outroUsuario = criarUsuario("cliente-2", UserRole.CLIENTE, 0);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, outroUsuario);
        solicitacao.setId("sol-1");

        when(solicitacaoRepository.findById("sol-1")).thenReturn(Optional.of(solicitacao));

        SolicitacaoResponseDTO resultado = solicitacaoService.findById("sol-1", gestor);

        assertEquals("sol-1", resultado.id());
        verify(solicitacaoRepository).findById("sol-1");
    }

    @Test
    void findByIdQuandoClienteDeveRetornarApenasSolicitacaoPropria() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 0);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, cliente);
        solicitacao.setId("sol-1");

        when(solicitacaoRepository.findByIdAndUsuario_Id("sol-1", "cliente-1"))
                .thenReturn(Optional.of(solicitacao));

        SolicitacaoResponseDTO resultado = solicitacaoService.findById("sol-1", cliente);

        assertEquals("sol-1", resultado.id());
        verify(solicitacaoRepository).findByIdAndUsuario_Id("sol-1", "cliente-1");
    }

    @Test
    void findByIdDeveLancarExcecaoQuandoNaoEncontrada() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 0);

        when(solicitacaoRepository.findByIdAndUsuario_Id("sol-x", "cliente-1"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> solicitacaoService.findById("sol-x", cliente));
    }


    @Test
    void createDeveSalvarERetornarSolicitacaoPendente() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 0);
        SolicitacaoCreateDTO dto = new SolicitacaoCreateDTO("Plantei árvores", CategoriaRecompensa.ACAO_LOCALIZADA);

        when(solicitacaoRepository.save(any(Solicitacao.class))).thenAnswer(invocacao -> {
            Solicitacao salva = invocacao.getArgument(0);
            salva.setId("sol-novo");
            return salva;
        });

        SolicitacaoResponseDTO resultado = solicitacaoService.create(dto, cliente);

        assertEquals("sol-novo", resultado.id());
        assertEquals("Plantei árvores", resultado.descricao());
        assertEquals(CategoriaRecompensa.ACAO_LOCALIZADA, resultado.categoria());
        assertEquals(SolicitacaoStatus.PENDENTE, resultado.status());
    }


    @Test
    void approveDeveAprovarECreditarPontosNoUsuario() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 10);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, cliente);
        solicitacao.setId("sol-1");

        when(solicitacaoRepository.findById("sol-1")).thenReturn(Optional.of(solicitacao));
        when(solicitacaoRepository.save(any(Solicitacao.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        SolicitacaoResponseDTO resultado = solicitacaoService.approve("sol-1");

        assertEquals(SolicitacaoStatus.APROVADO, resultado.status());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals(25, captor.getValue().getPontos()); // 10 + 15 (MICRO_ACAO)
    }

    @Test
    void approveDeveLancarExcecaoQuandoSolicitacaoNaoEncontrada() {
        when(solicitacaoRepository.findById("sol-x")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> solicitacaoService.approve("sol-x"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void approveDeveLancarExcecaoQuandoSolicitacaoJaFoiResolvida() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 10);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, cliente);
        solicitacao.setId("sol-1");
        solicitacao.setStatus(SolicitacaoStatus.APROVADO);

        when(solicitacaoRepository.findById("sol-1")).thenReturn(Optional.of(solicitacao));

        assertThrows(RuntimeException.class, () -> solicitacaoService.approve("sol-1"));
        verify(userRepository, never()).save(any());
    }


    @Test
    void rejectDeveReprovarSolicitacaoSemAlterarPontos() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 10);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, cliente);
        solicitacao.setId("sol-1");

        when(solicitacaoRepository.findById("sol-1")).thenReturn(Optional.of(solicitacao));
        when(solicitacaoRepository.save(any(Solicitacao.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        SolicitacaoResponseDTO resultado = solicitacaoService.reject("sol-1");

        assertEquals(SolicitacaoStatus.REPROVADO, resultado.status());
        verifyNoInteractions(userRepository);
    }

    @Test
    void rejectDeveLancarExcecaoQuandoSolicitacaoNaoEncontrada() {
        when(solicitacaoRepository.findById("sol-x")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class, () -> solicitacaoService.reject("sol-x"));
    }

    @Test
    void rejectDeveLancarExcecaoQuandoSolicitacaoJaFoiResolvida() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 10);
        Solicitacao solicitacao = new Solicitacao("Descrição", CategoriaRecompensa.MICRO_ACAO, cliente);
        solicitacao.setId("sol-1");
        solicitacao.setStatus(SolicitacaoStatus.REPROVADO);

        when(solicitacaoRepository.findById("sol-1")).thenReturn(Optional.of(solicitacao));

        assertThrows(RuntimeException.class, () -> solicitacaoService.reject("sol-1"));
    }
}