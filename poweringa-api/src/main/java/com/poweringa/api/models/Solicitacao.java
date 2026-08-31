package com.poweringa.api.models;

import com.poweringa.api.enums.SolicitacaoStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "solicitacoes")
public class Solicitacao {
    @Id
    private String id;
    private String descricao;
    private SolicitacaoStatus status = SolicitacaoStatus.PENDENTE;

    private User usuario;

    public Solicitacao(String descricao, User usuario) {
        this.descricao = descricao;
        this.usuario = usuario;
    }
}
