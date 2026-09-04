package com.poweringa.api.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItensVenda {
    private String idProduto;
    private String descricao;
    private Integer quantidade;
    private Integer totalPontosItem;

    public ItensVenda(String idProduto, String descricao, Integer quantidade, Integer totalPontosItem) {
        this.idProduto = idProduto;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.totalPontosItem = totalPontosItem;
    }
}
