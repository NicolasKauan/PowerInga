package com.poweringa.api.services;

import com.poweringa.api.dtos.VendaCreateDTO;
import com.poweringa.api.dtos.VendaItemCreateDTO;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.exceptions.ResourceNotFound;
import com.poweringa.api.models.ItensVenda;
import com.poweringa.api.models.ProdutosModel;
import com.poweringa.api.models.User;
import com.poweringa.api.models.Venda;
import com.poweringa.api.repositories.UserRepository;
import com.poweringa.api.repositories.VendaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {
    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProdutoService produtoService;

    public List<Venda> findAll(User usuarioLogado) {
        if (usuarioLogado.getCargo() == UserRole.GESTOR) {
            return vendaRepository.findAll();
        }

        return vendaRepository.findByUsuario_Id(usuarioLogado.getId());
    }

    public Venda findById(String id, User usuarioLogado) {
        ResourceNotFound exception = new ResourceNotFound("ERRO: Venda com esse id não foi encontrada");

        if (usuarioLogado.getCargo() == UserRole.GESTOR) {
            return vendaRepository.findById(id)
                    .orElseThrow(() -> exception);
        }

        return vendaRepository.findByIdAndUsuario_Id(id, usuarioLogado.getId())
                .orElseThrow(() -> exception);
    }

    public Venda save(VendaCreateDTO dto, User usuarioLogado) {
        List<ItensVenda> itensVenda = generateSaleItemsList(dto);

        Integer totalPontosVenda = calculateSaleTotalPoints(itensVenda);

        debitPointsFromUserBalance(totalPontosVenda, usuarioLogado);

        Venda venda = new Venda(usuarioLogado, itensVenda, totalPontosVenda, LocalDateTime.now());

        return vendaRepository.save(venda);
    }

    public List<ItensVenda> generateSaleItemsList(VendaCreateDTO dto){
        List<ItensVenda> itens = new ArrayList<>();

        for (VendaItemCreateDTO itemCreateDTO : dto.itens()) {
            ProdutosModel produto = produtoService.findById(itemCreateDTO.idProduto());

            ItensVenda itemVenda = new ItensVenda(
                    produto.getId(),
                    produto.getDescricao(),
                    itemCreateDTO.quantidade(),
                    produto.getValorPontos() * itemCreateDTO.quantidade()
            );

            itens.add(itemVenda);
        }

        return itens;
    }

    public void debitPointsFromUserBalance(Integer totalPontos, User usuarioLogado) {
        Integer saldoAtualUsuario = usuarioLogado.getPontos();

        if (totalPontos > saldoAtualUsuario) {
            throw new RuntimeException("ERRO: Valor da compra excede o saldo do usuário!");
        }

        usuarioLogado.setPontos(saldoAtualUsuario - totalPontos);

        userRepository.save(usuarioLogado);
    }

    public Integer calculateSaleTotalPoints(List<ItensVenda> itensVenda) {
        Integer totalPontosVenda = 0;

        for (ItensVenda itemVenda : itensVenda) {
            totalPontosVenda += itemVenda.getTotalPontosItem();
        }

        return totalPontosVenda;
    }
}
