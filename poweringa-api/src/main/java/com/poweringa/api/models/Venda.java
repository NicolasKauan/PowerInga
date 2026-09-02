package com.poweringa.api.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "vendas")
public class Venda {
    @Id
    private String id;

    private User usuario;

    private List<ItensVenda> itens;
    private Integer totalPontosVenda;
    private LocalDateTime created_at;

    public Venda(User usuario, List<ItensVenda> itens, Integer totalPontosVenda, LocalDateTime created_at) {
        this.usuario = usuario;
        this.itens = itens;
        this.totalPontosVenda = totalPontosVenda;
        this.created_at = created_at;
    }
}
