package com.poweringa.api.services;

import com.poweringa.api.dtos.VendaCreateDTO;
import com.poweringa.api.dtos.VendaItemCreateDTO;
import com.poweringa.api.dtos.VendaResponseDTO;
import com.poweringa.api.dtos.ProdutoResponseDTO;
import com.poweringa.api.enums.UserRole;
import com.poweringa.api.exceptions.BusinessRuleViolation;
import com.poweringa.api.exceptions.ResourceNotFound;
import com.poweringa.api.models.ItensVenda;
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

    public List<VendaResponseDTO> findAll(User usuarioLogado) {
        if (usuarioLogado.getCargo() == UserRole.GESTOR) {
            return vendaRepository.findAll()
                    .stream()
                    .map(this::toDTO)
                    .toList();
        }

        return vendaRepository.findByUsuario_Id(usuarioLogado.getId())
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public VendaResponseDTO findById(String id, User usuarioLogado) {
        ResourceNotFound exception = new ResourceNotFound("ERRO: Venda com esse id não foi encontrada");

        if (usuarioLogado.getCargo() == UserRole.GESTOR) {
            return toDTO(vendaRepository.findById(id)
                    .orElseThrow(() -> exception));
        }

        return toDTO(vendaRepository.findByIdAndUsuario_Id(id, usuarioLogado.getId())
                .orElseThrow(() -> exception));
    }

    public VendaResponseDTO save(VendaCreateDTO dto, User usuarioLogado) {
        List<ItensVenda> itensVenda = generateSaleItemsList(dto);

        Integer totalPontosVenda = calculateSaleTotalPoints(itensVenda);

        debitPointsFromUserBalance(totalPontosVenda, usuarioLogado);

        Venda venda = new Venda(usuarioLogado, itensVenda, totalPontosVenda, LocalDateTime.now());

        return toDTO(vendaRepository.save(venda));
    }

    public VendaResponseDTO toDTO(Venda venda) {
        return new VendaResponseDTO(
                venda.getId(),
                venda.getUsuario().getId(),
                venda.getItens(),
                venda.getTotalPontosVenda(),
                venda.getCreated_at()
        );
    }

    public List<ItensVenda> generateSaleItemsList(VendaCreateDTO dto){
        List<ItensVenda> itens = new ArrayList<>();

        for (VendaItemCreateDTO itemCreateDTO : dto.itens()) {
            ProdutoResponseDTO produto = produtoService.findById(itemCreateDTO.idProduto());

            // Produtos gravados antes da validação de ProdutoRequestDTO podem ter valor nulo ou negativo.
            if (produto.valorPontos() == null || produto.valorPontos() <= 0) {
                throw new IllegalStateException(
                        "ERRO: Produto " + produto.id() + " possui valor em pontos inválido!"
                );
            }

            ItensVenda itemVenda = new ItensVenda(
                    produto.id(),
                    produto.descricao(),
                    itemCreateDTO.quantidade(),
                    Math.multiplyExact(produto.valorPontos(), itemCreateDTO.quantidade())
            );

            itens.add(itemVenda);
        }

        return itens;
    }

    public void debitPointsFromUserBalance(Integer totalPontos, User usuarioLogado) {
        Integer saldoAtualUsuario = usuarioLogado.getPontos();

        if (totalPontos > saldoAtualUsuario) {
            throw new BusinessRuleViolation("ERRO: Valor da compra excede o saldo do usuário!");
        }

        usuarioLogado.setPontos(saldoAtualUsuario - totalPontos);

        userRepository.save(usuarioLogado);
    }

    public Integer calculateSaleTotalPoints(List<ItensVenda> itensVenda) {
        Integer totalPontosVenda = 0;

        for (ItensVenda itemVenda : itensVenda) {
            totalPontosVenda = Math.addExact(totalPontosVenda, itemVenda.getTotalPontosItem());
        }

        return totalPontosVenda;
    }
}
