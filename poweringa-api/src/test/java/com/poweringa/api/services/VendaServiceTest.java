package com.poweringa.api.services;

import com.poweringa.api.dtos.ProdutoResponseDTO;
import com.poweringa.api.dtos.VendaCreateDTO;
import com.poweringa.api.dtos.VendaItemCreateDTO;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.exceptions.ResourceNotFound;
import com.poweringa.api.models.ItensVenda;
import com.poweringa.api.models.User;
import com.poweringa.api.models.Venda;
import com.poweringa.api.repositories.UserRepository;
import com.poweringa.api.repositories.VendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private VendaService vendaService;

    @Test
    void findAllDeveRetornarTodasAsVendasQuandoUsuarioForGestor() {
        User gestor = criarUsuario("gestor-1", UserRole.GESTOR, 100);
        List<Venda> vendas = List.of(criarVenda(gestor));
        when(vendaRepository.findAll()).thenReturn(vendas);

        List<Venda> resultado = vendaService.findAll(gestor);

        assertSame(vendas, resultado);
        verify(vendaRepository).findAll();
        verify(vendaRepository, never()).findByUsuario_Id(any());
    }

    @Test
    void findAllDeveRetornarApenasVendasDoClienteLogado() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 100);
        List<Venda> vendas = List.of(criarVenda(cliente));
        when(vendaRepository.findByUsuario_Id(cliente.getId())).thenReturn(vendas);

        List<Venda> resultado = vendaService.findAll(cliente);

        assertSame(vendas, resultado);
        verify(vendaRepository).findByUsuario_Id("cliente-1");
        verify(vendaRepository, never()).findAll();
    }

    @Test
    void findByIdDeveRetornarVendaQuandoGestorSolicitar() {
        User gestor = criarUsuario("gestor-1", UserRole.GESTOR, 100);
        Venda venda = criarVenda(gestor);
        when(vendaRepository.findById("venda-1")).thenReturn(Optional.of(venda));

        Venda resultado = vendaService.findById("venda-1", gestor);

        assertSame(venda, resultado);
        verify(vendaRepository).findById("venda-1");
        verify(vendaRepository, never()).findByIdAndUsuario_Id(any(), any());
    }

    @Test
    void findByIdDeveRetornarVendaDoProprioCliente() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 100);
        Venda venda = criarVenda(cliente);
        when(vendaRepository.findByIdAndUsuario_Id("venda-1", "cliente-1")).thenReturn(Optional.of(venda));

        Venda resultado = vendaService.findById("venda-1", cliente);

        assertSame(venda, resultado);
        verify(vendaRepository).findByIdAndUsuario_Id("venda-1", "cliente-1");
        verify(vendaRepository, never()).findById(any());
    }

    @Test
    void findByIdDeveLancarExcecaoQuandoVendaDoGestorNaoExistir() {
        User gestor = criarUsuario("gestor-1", UserRole.GESTOR, 100);
        when(vendaRepository.findById("venda-inexistente")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class,
                () -> vendaService.findById("venda-inexistente", gestor));

        verify(vendaRepository).findById("venda-inexistente");
    }

    @Test
    void findByIdDeveLancarExcecaoQuandoVendaDoClienteNaoExistir() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 100);
        when(vendaRepository.findByIdAndUsuario_Id("venda-inexistente", "cliente-1"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFound.class,
                () -> vendaService.findById("venda-inexistente", cliente));

        verify(vendaRepository).findByIdAndUsuario_Id("venda-inexistente", "cliente-1");
    }

    @Test
    void generateSaleItemsListDeveConverterProdutosEmItensDaVenda() {
        VendaCreateDTO dto = new VendaCreateDTO(List.of(
                new VendaItemCreateDTO("produto-1", 2),
                new VendaItemCreateDTO("produto-2", 3)
        ));
        when(produtoService.findById("produto-1"))
                .thenReturn(new ProdutoResponseDTO("produto-1", "Camiseta", 15));
        when(produtoService.findById("produto-2"))
                .thenReturn(new ProdutoResponseDTO("produto-2", "Caneca", 10));

        List<ItensVenda> itens = vendaService.generateSaleItemsList(dto);

        assertEquals(2, itens.size());
        assertEquals("produto-1", itens.get(0).getIdProduto());
        assertEquals("Camiseta", itens.get(0).getDescricao());
        assertEquals(2, itens.get(0).getQuantidade());
        assertEquals(30, itens.get(0).getTotalPontosItem());
        assertEquals("produto-2", itens.get(1).getIdProduto());
        assertEquals(30, itens.get(1).getTotalPontosItem());
    }

    @Test
    void calculateSaleTotalPointsDeveSomarOsPontosDeCadaItem() {
        List<ItensVenda> itens = List.of(
                new ItensVenda("produto-1", "Camiseta", 2, 30),
                new ItensVenda("produto-2", "Caneca", 3, 30)
        );

        Integer total = vendaService.calculateSaleTotalPoints(itens);

        assertEquals(60, total);
    }

    @Test
    void debitPointsFromUserBalanceDeveAtualizarESalvarSaldoQuandoHouverPontosSuficientes() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 100);

        vendaService.debitPointsFromUserBalance(40, cliente);

        assertEquals(60, cliente.getPontos());
        verify(userRepository).save(cliente);
    }

    @Test
    void debitPointsFromUserBalanceNaoDeveSalvarQuandoSaldoForInsuficiente() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 20);

        RuntimeException excecao = assertThrows(RuntimeException.class,
                () -> vendaService.debitPointsFromUserBalance(40, cliente));

        assertTrue(excecao.getMessage().startsWith("ERRO:"));
        assertEquals(20, cliente.getPontos());
        verify(userRepository, never()).save(any());
    }

    @Test
    void saveDeveDebitarPontosEGuardarVendaMontadaAPartirDoDto() {
        User cliente = criarUsuario("cliente-1", UserRole.CLIENTE, 100);
        VendaCreateDTO dto = new VendaCreateDTO(List.of(new VendaItemCreateDTO("produto-1", 2)));
        when(produtoService.findById("produto-1"))
                .thenReturn(new ProdutoResponseDTO("produto-1", "Camiseta", 30));
        when(vendaRepository.save(any(Venda.class))).thenAnswer(invocacao -> invocacao.getArgument(0));
        LocalDateTime antesDaVenda = LocalDateTime.now();

        Venda resultado = vendaService.save(dto, cliente);
        LocalDateTime depoisDaVenda = LocalDateTime.now();

        ArgumentCaptor<Venda> vendaCaptor = ArgumentCaptor.forClass(Venda.class);
        verify(vendaRepository).save(vendaCaptor.capture());
        Venda vendaSalva = vendaCaptor.getValue();
        assertSame(vendaSalva, resultado);
        assertSame(cliente, vendaSalva.getUsuario());
        assertEquals(60, vendaSalva.getTotalPontosVenda());
        assertEquals(1, vendaSalva.getItens().size());
        assertEquals(60, vendaSalva.getItens().get(0).getTotalPontosItem());
        assertTrue(!vendaSalva.getCreated_at().isBefore(antesDaVenda));
        assertTrue(!vendaSalva.getCreated_at().isAfter(depoisDaVenda));
        assertEquals(40, cliente.getPontos());
        verify(userRepository).save(cliente);
    }

    private User criarUsuario(String id, UserRole cargo, Integer pontos) {
        User usuario = new User("usuario@email.com", "senha", cargo, "Usuario de teste");
        usuario.setId(id);
        usuario.setPontos(pontos);
        return usuario;
    }

    private Venda criarVenda(User usuario) {
        return new Venda(usuario, List.of(), 0, LocalDateTime.now());
    }
}
